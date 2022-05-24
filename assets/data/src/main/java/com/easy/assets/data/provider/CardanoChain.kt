package com.easy.assets.data.provider

import com.easy.assets.data.model.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.wallets.repository.WalletRepositoryImpl
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import java.lang.NullPointerException
import java.math.BigInteger

internal class CardanoChain(
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            ""
        }
    }

    override fun address(): String {
        return walletRepository.hdWallet.getAddressForCoin(coinType())
    }

    override fun coinType(): CoinType {
        return CoinType.CARDANO
    }

    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ONE
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<List<Transaction>> {
        return NetworkResponse.Error(NetworkResponseCode.checkError(NullPointerException()))
    }
}