package com.easy.assets.domain.repository

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.ValidationResult
import wallet.core.jni.CoinType

interface CoinRepository {
    fun address(slug: String): String
    fun coinType(slug: String): CoinType
    suspend fun assets(): List<AssetInfo>
}