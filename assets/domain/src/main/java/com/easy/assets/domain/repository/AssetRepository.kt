package com.easy.assets.domain.repository

import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.consts.NetworkChain
import java.math.BigInteger

interface AssetRepository {
    suspend fun signTransaction(chain: NetworkChain, plan: TransactionPlan): String
    suspend fun balance(chain: NetworkChain, contract: String?): BigInteger
    suspend fun transactions(
        chain: NetworkChain,
        offset: Int,
        limit: Int,
        contract: String?
    ): Result<List<Transaction>>

    suspend fun broadcastTransaction(chain: NetworkChain, data: String): Result<String>
}