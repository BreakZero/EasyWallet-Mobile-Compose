package com.easy.assets.data.provider

import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.GlobalHolder
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import java.lang.NullPointerException
import java.math.BigInteger

internal class BitcoinChain(
    private val ktorClient: HttpClient
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return withContext(Dispatchers.IO) {
            val amount = plan.amount
            val from = address()
            val usingMax = false
            val hexScript = ""
            val utxos = listOf<String>()
            ""
        }
    }

    override fun address(): String {
        return GlobalHolder.hdWallet.getAddressForCoin(CoinType.BITCOIN)
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