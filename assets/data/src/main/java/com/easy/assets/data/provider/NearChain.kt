package com.easy.assets.data.provider

import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.ext.toHexBytes
import com.easy.wallets.repository.WalletRepositoryImpl
import com.google.protobuf.ByteString
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.Base58
import wallet.core.jni.CoinType
import wallet.core.jni.proto.NEAR
import java.lang.NullPointerException
import java.math.BigInteger

internal class NearChain(
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val transferAction = NEAR.Transfer.newBuilder().apply {
                deposit = ByteString.copyFrom("01000000000000000000000000000000".toHexBytes())
            }.build()
            val signingInput = NEAR.SigningInput.newBuilder().apply {
                signerId = "dougie1.testnet"
                nonce = 1
                receiverId = "wrap.testnet"
                addActionsBuilder().apply {
                    transfer = transferAction
                }
                blockHash = ByteString.copyFrom(Base58.decodeNoCheck("244ZQ9cgj3CQ6bWBdytfrJMuMQ1jdXLFGnr4HhvtCTnM"))
                privateKey = ByteString.copyFrom(Base58.decodeNoCheck("3hoMW1HvnRLSFCLZnvPzWeoGwtdHzke34B2cTHM8rhcbG3TbuLKtShTv3DvyejnXKXKBiV7YPkLeqUHN1ghnqpFv").sliceArray(0..31))
            }.build()
            ""
        }
    }

    private fun addKey(): String {
        NEAR.AddKey.newBuilder().apply {
            this.publicKey = NEAR.PublicKey.newBuilder().apply {
                this.data = ByteString.copyFrom("".toHexBytes())
                this.keyType = 1
            }.build()
        }.build()
        return ""
    }

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(CoinType.BITCOIN)
    }

    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ONE
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<EthTxResponseDto> {
        return NetworkResponse.Error(NetworkResponseCode.checkError(NullPointerException()))
    }
}