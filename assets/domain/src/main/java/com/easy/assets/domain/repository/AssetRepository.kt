package com.easy.assets.domain.repository

import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import java.math.BigInteger

interface AssetRepository {
    suspend fun signTransaction(slug: String, plan: TransactionPlan): String
    suspend fun balance(slug: String, contract: String?): BigInteger
    suspend fun transactions(
        slug: String,
        offset: Int,
        limit: Int,
        contract: String?
    ): Result<List<Transaction>>

    suspend fun broadcastTransaction(slug: String, data: String): Result<String>
}