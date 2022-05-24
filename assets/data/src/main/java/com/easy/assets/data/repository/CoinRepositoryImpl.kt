package com.easy.assets.data.repository

import com.easy.assets.data.AssetsManager
import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.model.remote.dto.CoinConfigDto
import com.easy.assets.data.model.remote.dto.CoinConfigResponseDto
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.CoinRepository
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : CoinRepository {
    override fun address(slug: String): String {
        return assetsManager.find(slug).address()
    }

    override suspend fun assets(): List<AssetInfo> {
        return withContext(Dispatchers.IO) {
            assetsManager.fetchAssets()
        }
    }
}