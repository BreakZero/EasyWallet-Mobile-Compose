package com.easy.assets.data

import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.provider.BitcoinChain
import com.easy.assets.data.provider.DefaultChain
import com.easy.assets.data.provider.EthereumChain
import com.easy.assets.data.provider.IChain
import com.easy.assets.data.remote.dto.CoinConfigResponseDto
import com.easy.assets.domain.model.AssetInfo
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import logcat.logcat
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class AssetsManager @Inject constructor(
    private val ktorClient: HttpClient
) {
    private val mutex = Mutex()
    private val s = AtomicReference<Map<String, IChain>>()
    private val chains: MutableMap<String, IChain> = mutableMapOf()

    internal suspend fun fetchAssets(): List<AssetInfo> {
        return kotlin.runCatching {
            ktorClient.get<CoinConfigResponseDto>("http://45.153.130.249:8080/currencies")
        }.onFailure {
            it.printStackTrace()
        }.map { it.result }.getOrNull()?.let { it ->
            it.map { item ->
                mutex.withLock {
                    syncChains(item.coinSlug)
                }
                item.toAsset()
            }
        } ?: emptyList()
    }

    private fun syncChains(slug: String) {
        if (chains[slug] == null) {
            when (slug) {
                "ethereum", "erc20-uni",
                "erc20-dai", "erc20-cro" -> chains[slug] = EthereumChain(ktorClient)
                "bitcoin" -> chains[slug] = BitcoinChain(ktorClient)
                else -> Unit
            }
        }
    }

    internal fun find(slug: String): IChain {
        return chains[slug] ?: DefaultChain()
    }
}