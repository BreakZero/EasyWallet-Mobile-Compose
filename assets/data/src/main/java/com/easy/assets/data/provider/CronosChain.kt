package com.easy.assets.data.provider

import com.easy.assets.data.HttpRoutes
import com.easy.assets.data.errors.InsufficientBalanceException
import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.CallBalance
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.common.hex
import com.easy.core.ext._16toNumber
import com.easy.core.ext.clearHexPrefix
import com.easy.core.ext.toHexByteArray
import com.easy.wallets.repository.WalletRepositoryImpl
import com.google.protobuf.ByteString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger

internal class CronosChain(
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val balance = balance(plan.contract)
            val nonce = fetchNonce()
            val gasLimit = estimateGasLimit()
            if (balance < plan.amount) throw InsufficientBalanceException()
            val prvKey =
                ByteString.copyFrom(walletRepository.hdWallet.getKeyForCoin(CoinType.ETHEREUM).data())
            val signer = plan.contract?.let {
                val tokenTransfer = Ethereum.Transaction.ERC20Transfer.newBuilder().apply {
                    to = plan.to
                    amount = ByteString.copyFrom(plan.amount.toHexByteArray())
                }
                Ethereum.SigningInput.newBuilder().apply {
                    this.privateKey = prvKey
                    this.toAddress = it
                    this.chainId = ByteString.copyFrom("25".toBigInteger().toByteArray())
                    this.nonce = ByteString.copyFrom(nonce.toHexByteArray())
                    this.txMode = Ethereum.TransactionMode.Legacy
                    this.gasPrice = ByteString.copyFrom("100".toBigInteger().toHexByteArray())
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
                    this.gasPrice = ByteString.copyFrom("100".toBigInteger().toHexByteArray())
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

    private suspend fun fetchNonce() = withContext(Dispatchers.IO) {
        val reqBody = BaseRpcRequest(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_getTransactionCount",
            params = listOf(address(), "latest")
        )
        val nonce = ktorClient.post() {
            url(HttpRoutes.CRONOS_BASE_URL)
            setBody(reqBody)
        }.body<BaseRpcResponseDto<String>>().result
        nonce._16toNumber()
    }

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
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
            val response: BaseRpcResponseDto<String> = ktorClient.post {
                url(HttpRoutes.CRONOS_BASE_URL)
                setBody(reqBody)
            }.body()
            response.result.clearHexPrefix().toBigInteger(16)
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
        Timber.d(message = "offset: $offset, limit: $limit")
        val url = if (contract.isNullOrEmpty()) {
            """
            https://cronos.org/explorer/api?
            module=account
            &action=txlist
            &address=${address()}
            &page=$limit
            &offset=$offset
            &sort=desc
            """.trimIndent()
        } else {
            """
            https://cronos.org/explorer/api?
            module=account
            &action=tokentx
            &contractaddress=$contract
            &address=${address()}
            &page=$limit
            &offset=$offset
            &sort=desc
            """.trimIndent()
        }
        try {
            val response: EthTxResponseDto = ktorClient.get(urlString = url).body()
            NetworkResponse.Success(response)
        } catch (e: Throwable) {
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }
}
