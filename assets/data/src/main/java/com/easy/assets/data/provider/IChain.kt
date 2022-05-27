package com.easy.assets.data.provider

import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.common.NetworkResponse
import wallet.core.jni.CoinType
import java.math.BigInteger

internal interface IChain {
    suspend fun sign(plan: TransactionPlan): String
    fun address(): String
    fun coinType(): CoinType
    suspend fun balance(contract: String?): BigInteger
    suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<List<Transaction>>

    suspend fun broadcast(data: String): Result<String>
}