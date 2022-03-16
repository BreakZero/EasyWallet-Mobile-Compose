package com.easy.assets.data.repository

import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.remote.dto.CoinConfigDto
import com.easy.assets.data.remote.dto.CoinConfigResponseDto
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.GlobalHolder
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wallet.core.jni.CoinType
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val ktorClient: HttpClient
) : CoinRepository {
    private val _CoinConfigs = mutableListOf<CoinConfigDto>()

    override fun address(slug: String): String {
        return when (slug) {
            "bitcoin" -> GlobalHolder.hdWallet.getAddressForCoin(CoinType.BITCOIN)
            else -> GlobalHolder.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
        }
    }

    override suspend fun assets(): List<AssetInfo> {
        return withContext(Dispatchers.IO) {
            if (_CoinConfigs.isEmpty()) {
                kotlin.runCatching {
                    ktorClient.get<CoinConfigResponseDto>("http://45.153.130.249:8080/currencies")
                }.onFailure {
                    it.printStackTrace()
                }.map { it.result }.getOrNull()?.let { it ->
                    _CoinConfigs.clear()
                    _CoinConfigs.addAll(it)
                    it.map { item ->
                        item.toAsset()
                    }
                } ?: emptyList()
            } else {
                _CoinConfigs.map { item ->
                    item.toAsset()
                }
            }
        }
    }
}