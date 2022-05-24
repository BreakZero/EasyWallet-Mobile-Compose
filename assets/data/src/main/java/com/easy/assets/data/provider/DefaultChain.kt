package com.easy.assets.data.provider

import com.easy.assets.data.errors.UnSupportChainException
import com.easy.assets.data.model.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import wallet.core.jni.CoinType
import java.math.BigInteger

internal object DefaultChain: IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return ""
    }

    override fun address(): String {
        return ""
    }

    override fun coinType(): CoinType {
        return CoinType.BITCOIN
    }

    override suspend fun balance(contract: String?): BigInteger {
        return BigInteger.ZERO
    }

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<List<Transaction>> {
        return NetworkResponse.Error(NetworkResponseCode.checkError(UnSupportChainException()))
    }
}