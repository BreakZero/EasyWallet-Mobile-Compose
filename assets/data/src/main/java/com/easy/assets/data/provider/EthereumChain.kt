package com.easy.assets.data.provider

import android.util.Log
import com.easy.assets.data.HttpRoutes
import com.easy.assets.data.errors.InsufficientBalanceException
import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.CallBalance
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.data.remote.dto.FeeHistoryDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.BuildConfig
import com.easy.core.GlobalHolder
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.common.hex
import com.easy.core.ext.clearHexPrefix
import com.easy.core.ext.toHexByteArray
import com.google.protobuf.ByteString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger

internal class EthereumChain(
    private val ktorClient: HttpClient
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val balance = balance(plan.contract)
            val nonce = fetchNonce()
            val (baseFee, priorityFee) = feeHistory()
            val gasLimit = estimateGasLimit()
            Log.d("======", "$balance, $nonce, $baseFee, $priorityFee")
            if (balance < plan.amount) throw InsufficientBalanceException()
            val prvKey =
                ByteString.copyFrom(GlobalHolder.hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
            val signer = plan.contract?.let {
                val tokenTransfer = Ethereum.Transaction.ERC20Transfer.newBuilder().apply {
                    to = plan.to
                    amount = ByteString.copyFrom(plan.amount.toHexByteArray())
                }
                Ethereum.SigningInput.newBuilder().apply {
                    this.privateKey = prvKey
                    this.toAddress = it
                    this.chainId = ByteString.copyFrom("4".toBigInteger().toByteArray())
                    this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
                    this.txMode = Ethereum.TransactionMode.Enveloped
                    this.maxFeePerGas = ByteString.copyFrom(baseFee.toHexByteArray())
                    this.maxInclusionFeePerGas =
                        ByteString.copyFrom(priorityFee.toHexByteArray())
                    this.gasLimit = ByteString.copyFrom(gasLimit.toBigInteger().toHexByteArray())
                    this.transaction = Ethereum.Transaction.newBuilder().apply {
                        erc20Transfer = tokenTransfer.build()
                    }.build()
                }
            } ?: kotlin.run {
                val transfer = Ethereum.Transaction.Transfer.newBuilder().apply {
                    amount = ByteString.copyFrom(plan.amount.toHexByteArray())
                }
                Ethereum.SigningInput.newBuilder().apply {
                    this.privateKey = prvKey
                    this.toAddress = plan.to
                    this.chainId = ByteString.copyFrom("4".toBigInteger().toByteArray())
                    this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
                    this.txMode = Ethereum.TransactionMode.Enveloped
                    this.maxFeePerGas = ByteString.copyFrom(baseFee.toHexByteArray())
                    this.maxInclusionFeePerGas = ByteString.copyFrom(priorityFee.toHexByteArray())
                    this.gasLimit = ByteString.copyFrom(gasLimit.toBigInteger().toHexByteArray())
                    this.transaction = Ethereum.Transaction.newBuilder().apply {
                        this.transfer = transfer.build()
                    }.build()
                }
            }
            val output = AnySigner.sign(
                signer.build(),
                CoinType.ETHEREUM,
                Ethereum.SigningOutput.parser()
            )
            output.encoded.toByteArray().hex
        }
    }

    private suspend fun estimateGasLimit() = withContext(Dispatchers.IO) {
        21000L
    }

    private suspend fun feeHistory() = withContext(Dispatchers.IO) {
        val reqBody = BaseRpcRequest(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_feeHistory",
            params = listOf("0xF", "latest", listOf(25, 50, 75))
        )
        val feeHistories = ktorClient.post<BaseRpcResponseDto<FeeHistoryDto>>(
            urlString = HttpRoutes.INFURA_RPC
        ) {
            body = reqBody
        }.result
        val baseFee = feeHistories.baseFeePerGas.map {
            it.clearHexPrefix().toBigInteger(16)
        }.maxOrNull() ?: BigInteger.ZERO
        Pair(baseFee.times("1000000000".toBigInteger()), baseFee.times("1000000000".toBigInteger()))
    }

    private suspend fun fetchNonce() = withContext(Dispatchers.IO) {
        val reqBody = BaseRpcRequest(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_getTransactionCount",
            params = listOf(address(), "latest")
        )
        val nonce = ktorClient.post<BaseRpcResponseDto<String>>(
            urlString = HttpRoutes.INFURA_RPC
        ) {
            body = reqBody
        }.result
        nonce.clearHexPrefix().toBigInteger(16)
    }

    override fun address(): String {
        return GlobalHolder.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
    }

    override suspend fun balance(contract: String?) = withContext(Dispatchers.IO) {
        try {
            val reqBody = if (contract.isNullOrEmpty()) {
                BaseRpcRequest(
                    id = 1,
                    jsonrpc = "2.0",
                    method = "eth_getBalance",
                    params = listOf(address(), "latest")
                )
            } else {
                BaseRpcRequest(
                    id = 1,
                    jsonrpc = "2.0",
                    method = "eth_call",
                    params = listOf(
                        CallBalance(
                            from = address(),
                            to = contract,
                            data = "0x70a08231000000000000000000000000${address().clearHexPrefix()}"
                        ), "latest"
                    )
                )
            }
            val response = ktorClient
                .post<HttpResponse>(HttpRoutes.INFURA_RPC) {
                    body = reqBody
                }
            response.receive<BaseRpcResponseDto<String>>().result.clearHexPrefix()
                .toBigInteger(16)
        } catch (e: Exception) {
            e.printStackTrace()
            BigInteger.ZERO
        }
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<EthTxResponseDto> = withContext(Dispatchers.IO) {
        val url = if (contract.isNullOrEmpty()) {
            """
            https://api.etherscan.io/api?
            module=account
            &action=txlist
            &address=${address()}
            &page=1
            &offset=$offset
            &sort=desc
            &apikey=${BuildConfig.ETHERSCAN_APIKEY}
            """.trimIndent()
        } else {
            """
            https://api.etherscan.io/api?
            module=account
            &action=tokentx
            &contractaddress=$contract
            &address=${address()}
            &page=1
            &offset=$offset
            &sort=desc
            &apikey=${BuildConfig.ETHERSCAN_APIKEY}
            """.trimIndent()
        }
        try {
            val response = ktorClient.get<HttpResponse>(url)
            NetworkResponse.Success(response.receive())
        } catch (e: Throwable) {
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }
}