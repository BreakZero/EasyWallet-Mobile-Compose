package com.easy.assets.data.provider

import androidx.datastore.core.DataStore
import com.easy.assets.data.HttpRoutes
import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.data.model.remote.BaseRpcRequest
import com.easy.assets.data.model.remote.EthCall
import com.easy.assets.data.model.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.model.remote.dto.EthTxResponseDto
import com.easy.assets.domain.errors.InsufficientBalanceException
import com.easy.assets.domain.errors.UnSupportChainException
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.BuildConfig
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.common.hex
import com.easy.core.enums.Chain
import com.easy.core.enums.ChainNetwork
import com.easy.core.ext._16toNumber
import com.easy.core.ext.clearHexPrefix
import com.easy.core.ext.toHexByteArray
import com.easy.core.model.AppSettings
import com.easy.wallets.repository.WalletRepositoryImpl
import com.google.protobuf.ByteString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import java.math.BigInteger

internal class CronosChain(
    private val appSettings: DataStore<AppSettings>,
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val chainId = getChainId()
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
                    this.chainId = ByteString.copyFrom(chainId.toHexByteArray())
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
                    this.chainId = ByteString.copyFrom(chainId.toHexByteArray())
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

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(coinType())
    }

    override fun coinType(): CoinType {
        return CoinType.ETHEREUM
    }

    override suspend fun balance(contract: String?): BigInteger = withContext(Dispatchers.IO) {
        val rpc = getRpc()
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
                        EthCall(
                            from = address(),
                            to = contract,
                            data = "0x70a08231000000000000000000000000${address().clearHexPrefix()}"
                        ),
                        "latest"
                    )
                )
            }
            val response: BaseRpcResponseDto<String> = ktorClient.post {
                url(rpc)
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
    ): NetworkResponse<List<Transaction>> = withContext(Dispatchers.IO) {
        val explorerUrl = getExplorerUrl()
        try {
            val response: EthTxResponseDto = ktorClient.get {
                url(explorerUrl)
                parameter("module", "account")
                parameter("action", "txlist")
                parameter("address", address())
                parameter("page", limit)
                parameter("offset", offset)
                parameter("sort", "desc")
                parameter("apikey", BuildConfig.ETHERSCAN_APIKEY)
                if (!contract.isNullOrEmpty()) {
                    parameter("contractaddress", contract)
                }
            }.body()
            NetworkResponse.Success(
                response.result.map {
                    it.toTransaction(
                        address()
                    )
                }
            )
        } catch (e: Throwable) {
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }

    override suspend fun broadcast(data: String): Result<String> {
        return Result.failure(UnSupportChainException())
    }

    private suspend fun estimateGasLimit() = withContext(Dispatchers.IO) {
        21000L
    }

    private suspend fun fetchNonce() = withContext(Dispatchers.IO) {
        val rpc = getRpc()
        val reqBody = BaseRpcRequest(
            id = 1,
            jsonrpc = "2.0",
            method = "eth_getTransactionCount",
            params = listOf(address(), "latest")
        )
        val nonce = ktorClient.post() {
            url(rpc)
            setBody(reqBody)
        }.body<BaseRpcResponseDto<String>>().result
        nonce._16toNumber()
    }

    private suspend fun getChainId(): Int {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> Chain.CRONOS.id
            else -> Chain.CRONOS_TEST.id
        }
    }

    private suspend fun getRpc(): String {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> HttpRoutes.CRONOS_MAINNET_RPC
            else -> HttpRoutes.CRONOS_TESTNET_RPC
        }
    }

    private suspend fun getExplorerUrl(): String {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> HttpRoutes.CRONOS_MAINNET_EXPLORER
            else -> HttpRoutes.CRONOS_TESTNET_EXPLORER
        }
    }
}
