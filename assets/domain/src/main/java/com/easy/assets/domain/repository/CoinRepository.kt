package com.easy.assets.domain.repository

import com.easy.assets.domain.model.AssetInfo
import com.easy.core.consts.NetworkChain
import wallet.core.jni.CoinType

interface CoinRepository {
    fun address(chain: NetworkChain): String
    fun coinType(chain: NetworkChain): CoinType
    suspend fun assets(): List<AssetInfo>
}
