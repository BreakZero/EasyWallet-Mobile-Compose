package com.easy.assets.domain.repository

import com.easy.assets.domain.model.AssetInfo

interface CoinRepository {
    fun address(slug: String): String
    suspend fun assets(): List<AssetInfo>
}