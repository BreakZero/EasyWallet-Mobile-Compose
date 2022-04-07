package com.easy.assets.presentation

import app.cash.turbine.test
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
import com.easy.assets.domain.use_case.*
import com.easy.assets.presentation.assets.AssetEvent
import com.easy.assets.presentation.assets.AssetState
import com.easy.assets.presentation.assets.AssetUIEvent
import com.easy.assets.presentation.assets.WalletAssetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import java.math.BigInteger
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class AssetViewModelTest {
    private lateinit var assetViewModel: WalletAssetViewModel
    private lateinit var assetsUseCases: AssetsUseCases
    private lateinit var assetRepository: AssetRepository
    private lateinit var coinRepository: CoinRepository

    private val mockAsset = AssetInfo(
        slug = "erc20-uni",
        symbol = "UNI",
        contractAddress = "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984",
        decimal = 18,
        icon = "https://easywallet.s3.amazonaws.com/wallet-icons/UNI_4x.png",
        tag = "ERC20",
        balance = "0.0"
    )

    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        assetRepository = mock() {
            onBlocking { balance("", "") }.doReturn(BigInteger.ZERO)
            onBlocking {
                balance(
                    "erc20-uni",
                    "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984"
                )
            }.doReturn(
                BigInteger.TEN
            )
        }
        coinRepository = mock() {
            on { address("") }.doReturn("mock-address")
            onBlocking { assets() }.doReturn(listOf(mockAsset))
        }

        assetsUseCases = AssetsUseCases(
            address = CoinAddress(coinRepository),
            assets = Assets(coinRepository),
            assetsWithBalance = AssetsWithBalance(coinRepository, assetRepository),
            balance = AssetBalance(assetRepository),
            transactions = AssetTransactions(assetRepository),
            signTransaction = SignTransaction(assetRepository)
        )
        assetViewModel = WalletAssetViewModel(assetsUseCases)
    }

    @Test
    fun `test uiEvent Flow`() = runTest {
        assetViewModel.onEvent(AssetEvent.OnSend)
        assetViewModel.onEvent(AssetEvent.OnReceive)
        assetViewModel.onEvent(AssetEvent.OnItemClick(assetInfo = mockAsset))
        assetViewModel.uiEvent.test {
            val firstItem = awaitItem()
            assertEquals(AssetUIEvent.OnSendClick, firstItem)
            val secondItem = awaitItem()
            assertEquals(AssetUIEvent.OnReceiveClick, secondItem)
            val thirdItem = awaitItem()
            assertEquals(AssetUIEvent.OnItemClick(mockAsset), thirdItem)
        }
    }

    @Test
    fun `test state`() = runTest {
        assertEquals(AssetState(isLoading = true), assetViewModel.assetState)
    }

    @Test
    fun `test coin address`() = runTest {
        assertEquals("mock-address", assetsUseCases.address(""))
    }

    @Test
    fun `test coin balance`() = runTest {
        assertEquals(BigInteger.ZERO, assetsUseCases.balance("", ""))
        assertEquals(
            BigInteger.TEN, assetsUseCases.balance(
                "erc20-uni",
                "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984"
            )
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}