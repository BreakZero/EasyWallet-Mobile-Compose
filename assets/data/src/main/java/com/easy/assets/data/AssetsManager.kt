package com.easy.assets.data

import androidx.datastore.core.DataStore
import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.model.remote.dto.CoinConfigDto
import com.easy.assets.data.model.remote.dto.CoinConfigResponseDto
import com.easy.assets.data.provider.*
import com.easy.assets.domain.model.AssetInfo
import com.easy.core.consts.EasyAssetSlug
import com.easy.core.model.AppSettings
import com.easy.wallets.repository.WalletRepositoryImpl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.collections.set

private val default_coins = listOf(
    CoinConfigDto(
        coinSlug = "ethereum",
        coinSymbol = "ETH",
        contractAddress = null,
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/ethereum.png",
        tag = null
    ),
    CoinConfigDto(
        coinSlug = "erc20-dai",
        coinSymbol = "DAI",
        contractAddress = "0x6b175474e89094c44da98b954eedeac495271d0f",
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/DAIxxxhdpi.png",
        tag = "ERC20"
    ),
    CoinConfigDto(
        coinSlug = "erc20-cro",
        coinSymbol = "CRO",
        contractAddress = "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b",
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/cro.png",
        tag = "ERC20"
    ),
    CoinConfigDto(
        coinSlug = "solana",
        coinSymbol = "SOL",
        contractAddress = null,
        decimal = 9,
        iconUrl = "https://cryptologos.cc/logos/solana-sol-logo.png",
        tag = null
    ),
)

class AssetsManager @Inject constructor(
    private val ktorClient: HttpClient,
    private val appSettings: DataStore<AppSettings>,
    private val walletRepository: WalletRepositoryImpl
) {
    private val mutex = Mutex()
    private val chains: MutableMap<String, IChain> = mutableMapOf()

    internal suspend fun fetchAssets(): List<AssetInfo> {
        return kotlin.runCatching {
            val responseDto: CoinConfigResponseDto = ktorClient.get(
                urlString = "http://141.164.63.231:8080/currencies"
            ).body()
            responseDto.result
        }.getOrElse {
            default_coins
        }.map { item ->
            mutex.withLock {
                syncChains(item)
            }
            item.toAsset()
        }.sortedBy { it.tag }
    }

    private fun syncChains(config: CoinConfigDto) {
        val slug = config.coinSlug
        if (chains[slug] == null) {
            when {
                slug == EasyAssetSlug.SLUG_ETHEREUM ||
                        config.tag.equals(
                            EasyAssetSlug.TAG_ERC20,
                            true
                        ) -> {
                    chains[slug] = EthereumChain(appSettings, ktorClient, walletRepository)
                }
                slug == EasyAssetSlug.SLUG_BITCOIN -> {
                    chains[slug] = BitcoinChain(ktorClient, walletRepository)
                }
                slug == EasyAssetSlug.SLUG_CARDANO -> {
                    chains[slug] = CardanoChain(ktorClient, walletRepository)
                }
                slug == EasyAssetSlug.SLUG_POLYGON -> {
                    chains[slug] = PolygonChain(appSettings, ktorClient, walletRepository)
                }
                slug == EasyAssetSlug.SLUG_CRONOS -> {
                    chains[slug] = CronosChain(appSettings, ktorClient, walletRepository)
                }
                slug == EasyAssetSlug.SLUG_SOLANA -> {
                    chains[slug] = SolanaChain(appSettings, ktorClient, walletRepository)
                }
                else -> Unit
            }
        }
    }

    internal fun find(slug: String): IChain {
        return chains[slug] ?: DefaultChain
    }
}