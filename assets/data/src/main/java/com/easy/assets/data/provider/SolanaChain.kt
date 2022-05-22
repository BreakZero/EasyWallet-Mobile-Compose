package com.easy.assets.data.provider

import androidx.datastore.core.DataStore
import com.easy.assets.data.HttpRoutes
import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.data.remote.dto.RecentBlockHashResult
import com.easy.assets.data.remote.dto.SolBalanceDto
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
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Solana
import java.lang.NullPointerException
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
            val result: BaseRpcResponseDto<RecentBlockHashResult> = ktorClient.post {
                url(getRpc())
                setBody(reqBody)
            }.body()
            val recentBlock = result.result.value.blockhash
            val prvKey = ByteString.copyFrom(walletRepository.hdWallet.getKeyForCoin(CoinType.SOLANA).data())
            val transferMessage = Solana.Transfer.newBuilder().apply {
                recipient = plan.to
                value = plan.amount.toLong()
            }.build()
            val signingInput = Solana.SigningInput.newBuilder().apply {
                transferTransaction = transferMessage
                recentBlockhash = recentBlock
                privateKey = prvKey
            }.build()

            val output = AnySigner.sign(signingInput, CoinType.SOLANA, Solana.SigningOutput.parser())
            val rawData = output.encoded
            rawData
        }
    }

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(CoinType.SOLANA)
    }

    override suspend fun balance(contract: String?): BigInteger {
        val reqbody = BaseRpcRequest(
            jsonrpc = "2.0",
            id = 1,
            method = "getBalance",
            params = listOf(address())
        )
        val resp: BaseRpcResponseDto<SolBalanceDto> = ktorClient.post {
            url(getRpc())
            setBody(reqbody)
        }.body()

        return resp.result.value.toBigInteger()
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<List<Transaction>> {
        return NetworkResponse.Error(NetworkResponseCode.checkError(NullPointerException()))
    }

    private suspend fun getRpc(): String {
        return when (appSettings.data.first().network) {
            ChainNetwork.MAIN -> HttpRoutes.SOLANA_MAINNET_RPC
            else -> HttpRoutes.SOLANA_TESTNET_RPC
        }
    }
}