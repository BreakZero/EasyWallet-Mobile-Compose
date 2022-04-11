package com.easy.assets.data

import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.provider.*
import com.easy.assets.data.provider.BitcoinChain
import com.easy.assets.data.provider.CardanoChain
import com.easy.assets.data.provider.DefaultChain
import com.easy.assets.data.provider.EthereumChain
import com.easy.assets.data.provider.IChain
import com.easy.assets.data.remote.dto.CoinConfigDto
import com.easy.assets.data.remote.dto.CoinConfigResponseDto
import com.easy.assets.domain.model.AssetInfo
import com.easy.wallets.repository.WalletRepositoryImpl
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.collections.set

class AssetsManager @Inject constructor(
    private val ktorClient: HttpClient,
    private val walletRepository: WalletRepositoryImpl
) {
    private val mutex = Mutex()
    private val chains: MutableMap<String, IChain> = mutableMapOf()

    internal suspend fun fetchAssets(): List<AssetInfo> {
        return kotlin.runCatching {
            ktorClient.get<CoinConfigResponseDto>("http://141.164.63.231:8080/currencies")
        }.onFailure {
            it.printStackTrace()
        }.map { it.result }.getOrNull()?.let { it ->
            it.map { item ->
                mutex.withLock {
                    syncChains(item)
                }
                item.toAsset()
            }
        }?.sortedBy { it.tag } ?: listOf(
            CoinConfigDto(
                coinSlug = "ethereum",
                coinSymbol = "ETH",
                contractAddress = null,
                decimal = 18,
                iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/ethereum.png",
                tag = null
            )
        ).map {
            syncChains(it)
            it.toAsset()
        }
    }

    private fun syncChains(config: CoinConfigDto) {
        val slug = config.coinSlug
        if (chains[slug] == null) {
            when {
                slug == "ethereum" || config.tag.equals("ERC20", true) -> {
                    chains[slug] = EthereumChain(ktorClient, walletRepository)
                }
                slug == "bitcoin" -> chains[slug] = BitcoinChain(ktorClient, walletRepository)
                slug == "cardano" -> chains[slug] = CardanoChain(ktorClient, walletRepository)
                slug == "polygon" -> chains[slug] = PolygonChain(ktorClient, walletRepository)
                slug == "cronos" -> chains[slug] = CronosChain(ktorClient, walletRepository)
                else -> Unit
            }
        }
    }

    internal fun find(slug: String): IChain {
        return chains[slug] ?: DefaultChain
    }
}