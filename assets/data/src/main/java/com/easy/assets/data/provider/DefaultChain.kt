package com.easy.assets.data.provider

import com.easy.assets.data.errors.UnSupportChainException
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import java.math.BigInteger

internal class DefaultChain: IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        return ""
    }

    override fun address(): String {
        return ""
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