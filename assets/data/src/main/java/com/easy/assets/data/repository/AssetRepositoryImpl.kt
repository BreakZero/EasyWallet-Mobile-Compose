package com.easy.assets.data.repository

import com.easy.assets.data.AssetsManager
import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.common.NetworkResponse
import java.math.BigInteger
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : AssetRepository {
    override suspend fun signTransaction(slug: String, plan: TransactionPlan): String {
        return assetsManager.find(slug).sign(plan)
    }

    override suspend fun balance(slug: String, contract: String?): BigInteger {
        return assetsManager.find(slug).balance(contract)
    }

    override suspend fun transactions(
        slug: String,
        offset: Int,
        limit: Int,
        contract: String?
    ): Result<List<Transaction>> {
        return when (val result = assetsManager.find(slug).transactions(offset, limit, contract)) {
            is NetworkResponse.Success -> Result.success(result.data)
            is NetworkResponse.Error -> Result.failure(IllegalArgumentException(result.code.toString()))
            else -> Result.failure(UnknownError())
        }
    }

    override suspend fun broadcastTransaction(slug: String, data: String): Result<String> {
        return assetsManager.find(slug).broadcast(data)
    }
}