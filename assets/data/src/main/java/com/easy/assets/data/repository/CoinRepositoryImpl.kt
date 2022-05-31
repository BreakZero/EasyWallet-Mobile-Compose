package com.easy.assets.data.repository

import com.easy.assets.data.AssetsManager
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.consts.NetworkChain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val assetsManager: AssetsManager
) : CoinRepository {
    override fun address(chain: NetworkChain): String {
        return assetsManager.find(chain).address()
    }

    override fun coinType(chain: NetworkChain): CoinType {
        return assetsManager.find(chain).coinType()
    }

    override suspend fun assets(): List<AssetInfo> {
        return withContext(Dispatchers.IO) {
            assetsManager.fetchAssets()
        }
    }
}