package com.easy.assets.data.provider

import com.easy.assets.data.errors.UnSupportChainException
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.GlobalHolder
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import io.ktor.client.*
import wallet.core.jni.CoinType
import java.math.BigInteger

internal class PolygonChain(
    private val ktorClient: HttpClient
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        TODO("Not yet implemented")
    }

    override fun address(): String {
        return GlobalHolder.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
    }

    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ZERO
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<EthTxResponseDto> {
        return NetworkResponse.Error(NetworkResponseCode.checkError(UnSupportChainException()))
    }
}