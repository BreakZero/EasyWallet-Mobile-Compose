package com.easy.assets.data.repository

import com.easy.assets.data.AssetsManager
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.common.NetworkResponse
import com.easy.core.consts.NetworkChain
import java.math.BigInteger
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : AssetRepository {
    override suspend fun signTransaction(chain: NetworkChain, plan: TransactionPlan): String {
        return assetsManager.find(chain).sign(plan)
    }

    override suspend fun balance(chain: NetworkChain, contract: String?): BigInteger {
        return assetsManager.find(chain).balance(contract)
    }

    override suspend fun transactions(
        chain: NetworkChain,
        offset: Int,
        limit: Int,
        contract: String?
    ): Result<List<Transaction>> {
        return when (val result = assetsManager.find(chain).transactions(offset, limit, contract)) {
            is NetworkResponse.Success -> Result.success(result.data)
            is NetworkResponse.Error -> Result.failure(IllegalArgumentException(result.code.toString()))
            else -> Result.failure(UnknownError())
        }
    }

    override suspend fun broadcastTransaction(chain: NetworkChain, data: String): Result<String> {
        return assetsManager.find(chain).broadcast(data)
    }
}
