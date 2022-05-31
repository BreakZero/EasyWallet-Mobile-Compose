package com.easy.assets.data.provider

import androidx.datastore.core.DataStore
import com.easy.assets.data.HttpRoutes
import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.data.model.remote.BaseRpcRequest
import com.easy.assets.data.model.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.model.remote.dto.RecentBlockHashResult
import com.easy.assets.data.model.remote.dto.SolBalanceDto
import com.easy.assets.data.model.remote.dto.SolTransactionDto
import com.easy.assets.domain.errors.UnSupportChainException
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.enums.ChainNetwork
import com.easy.core.model.AppSettings
import com.easy.wallets.repository.WalletRepositoryImpl
import com.google.protobuf.ByteString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.SolanaAddress
import wallet.core.jni.proto.Solana
import java.math.BigInteger

internal class SolanaChain(
    private val appSettings: DataStore<AppSettings>,
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val reqBody = BaseRpcRequest(
                jsonrpc = "2.0",
                id = 1,
                method = "getRecentBlockhash",
                params = emptyList<String>()
            )
            try {
                val result: BaseRpcResponseDto<RecentBlockHashResult> = ktorClient.post {
                    url(getRpc())
                    setBody(reqBody)
                }.body()
                val recentBlock = result.result.value.blockhash
                val prvKey = ByteString.copyFrom(
                    walletRepository.hdWallet.getKeyForCoin(CoinType.SOLANA).data()
                )
                val tokenTransfer = Solana.TokenTransfer.newBuilder().apply {
                    this.amount = 100L
                    this.decimals = 2
                    this.tokenMintAddress = "AAzFMWxQxqypxSzrKZi8in43Cdwb9Zd2akc12pUBFqCn"
                    this.senderTokenAddress = "EoFannXdytn52ECbXVH2GcCJsnh83gTz72Y4yoacq1MG"
                    this.recipientTokenAddress = "G3CevsdXfi2BsLWGgQ5LSfRjciAN5HgdzC5fk2ApbyX8"
                }.build()
                val tokenAddress = ""
                val createAddress = SolanaAddress(address()).defaultTokenAddress(tokenAddress)
                val tokenCreateAndTransfer = Solana.CreateAndTransferToken.newBuilder().apply {
                    this.tokenMintAddress = ""
                    this.recipientMainAddress = createAddress
                    this.senderTokenAddress = ""
                    this.recipientTokenAddress = ""
                    this.amount = 100L
                    this.decimals = 6
                }
                /*val transferMessage = Solana.Transfer.newBuilder().apply {
                    recipient = plan.to
                    value = plan.amount.toLong()
                }.build()*/
                val signingInput = Solana.SigningInput.newBuilder().apply {
                    this.tokenTransferTransaction = tokenTransfer
                    recentBlockhash = recentBlock
                    privateKey = prvKey
                }.build()

                val output =
                    AnySigner.sign(signingInput, CoinType.SOLANA, Solana.SigningOutput.parser())
                val rawData = output.encoded
                rawData
            } catch (e: Exception) {
                Timber.e(e)
                ""
            }
        }
    }

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(coinType())
    }

    override fun coinType(): CoinType {
        return CoinType.SOLANA
    }

    override suspend fun balance(contract: String?): BigInteger {
        val reqbody = BaseRpcRequest(
            jsonrpc = "2.0",
            id = 1,
            method = "getBalance",
            params = listOf(address())
        )
        return try {
            val resp: BaseRpcResponseDto<SolBalanceDto> = ktorClient.post {
                url(getRpc())
                setBody(reqbody)
            }.body()
            resp.result.value.toBigInteger()
        } catch (e: java.lang.Exception) {
            BigInteger.ZERO
        }
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<List<Transaction>> {
        val explorerUrl = getExplorerUrl()
        return try {
            val response: List<SolTransactionDto> = ktorClient.get {
                url("$explorerUrl/account/transactions")
                parameter("account", address())
                parameter("limit", limit)
            }.body()
            NetworkResponse.Success(
                response.map {
                    it.toTransaction(address())
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }

    override suspend fun broadcast(data: String): Result<String> {
        val requestBody = BaseRpcRequest(
            id = 1,
            jsonrpc = "2.0",
            method = "sendTransaction",
            params = listOf(data)
        )

        return try {
            val response: BaseRpcResponseDto<String> = ktorClient.post {
                url(getRpc())
                setBody(requestBody)
            }.body()
            if (response.error != null) {
                Result.failure(RuntimeException(response.error.message))
            } else {
                Result.success(response.result)
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(UnSupportChainException())
        }
    }

    private suspend fun getRpc(): String {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> HttpRoutes.SOLANA_MAINNET_RPC
            else -> HttpRoutes.SOLANA_TESTNET_RPC
        }
    }
    private suspend fun getExplorerUrl(): String {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> HttpRoutes.SOLANA_MAINNET_EXPLORER
            else -> HttpRoutes.SOLANA_TESTNET_EXPLORER
        }
    }
}
