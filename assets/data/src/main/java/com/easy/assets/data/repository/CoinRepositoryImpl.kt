package com.easy.assets.data.repository

import com.easy.assets.data.AssetsManager
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : CoinRepository {
    override fun address(slug: String): String {
        return assetsManager.find(slug).address()
    }

    override fun coinType(slug: String): CoinType {
        return assetsManager.find(slug).coinType()
    }

    override suspend fun assets(): List<AssetInfo> {
        return withContext(Dispatchers.IO) {
            assetsManager.fetchAssets()
        }
    }
}