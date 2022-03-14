package com.easy.assets.domain.repository

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction
import com.easy.core.consts.ChainId
import java.math.BigInteger

interface AssetRepository {
    fun address(slug: String, legacy: Boolean): String
    suspend fun signTransaction(slug: String): String
    suspend fun balance(address: String, chainId: ChainId, contractAddress: String): BigInteger
    suspend fun transactions(
        address: String,
        chainId: ChainId,
        offset: Int,
        limit: Int,
        contractAddress: String
    ): Result<List<Transaction>>

    suspend fun assets(): Result<Map<String, List<AssetInfo>>>
}