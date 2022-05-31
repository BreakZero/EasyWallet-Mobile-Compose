package com.easy.assets.data

import androidx.datastore.core.DataStore
import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.model.remote.dto.CoinConfigDto
import com.easy.assets.data.model.remote.dto.CoinConfigResponseDto
import com.easy.assets.data.provider.*
import com.easy.assets.domain.model.AssetInfo
import com.easy.core.consts.AssetChain
import com.easy.core.consts.AssetTag
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
        chain = AssetChain.Ethereum,
        coinSymbol = "ETH",
        contractAddress = null,
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/ethereum.png",
        tag = null
    ),
    CoinConfigDto(
        chain = AssetChain.Ethereum,
        coinSymbol = "DAI",
        contractAddress = "0x6b175474e89094c44da98b954eedeac495271d0f",
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/DAIxxxhdpi.png",
        tag = AssetTag.ERC20
    ),
    CoinConfigDto(
        chain = AssetChain.Ethereum,
        coinSymbol = "CRO",
        contractAddress = "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b",
        decimal = 18,
        iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/cro.png",
        tag = AssetTag.ERC20
    ),
    CoinConfigDto(
        chain = AssetChain.Solana,
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
        val slug = config.slug()
        val chain = config.chain
        if (chains[slug] == null) {
            when {
                chain == AssetChain.Ethereum ||
                        config.tag == AssetTag.ERC20 -> {
                    chains[slug] = EthereumChain(appSettings, ktorClient, walletRepository)
                }
                chain == AssetChain.Bitcoin -> {
                    chains[slug] = BitcoinChain(ktorClient, walletRepository)
                }
                chain == AssetChain.Cardano -> {
                    chains[slug] = CardanoChain(ktorClient, walletRepository)
                }
                chain == AssetChain.Polygon -> {
                    chains[slug] = PolygonChain(appSettings, ktorClient, walletRepository)
                }
                chain == AssetChain.Cronos -> {
                    chains[slug] = CronosChain(appSettings, ktorClient, walletRepository)
                }
                chain == AssetChain.Solana -> {
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