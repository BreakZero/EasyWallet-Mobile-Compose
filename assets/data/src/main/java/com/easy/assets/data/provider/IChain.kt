package com.easy.assets.data.provider

import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import java.math.BigInteger

internal interface IChain {
    suspend fun sign(plan: TransactionPlan): String
    fun address(): String
    suspend fun balance(contract: String?): BigInteger
    suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<EthTxResponseDto>
}