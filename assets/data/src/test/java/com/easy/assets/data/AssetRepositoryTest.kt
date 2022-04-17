package com.easy.assets.data

import androidx.datastore.core.DataStore
import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.core.enums.ChainNetwork
import com.easy.core.model.AppSettings
import com.easy.core.model.EasyCurrency
import com.easy.wallets.data.WalletDao
import com.easy.wallets.repository.WalletRepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.io.File
import java.math.BigInteger
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AssetRepositoryTest {

    @Mock
    private lateinit var hdWallet: HDWallet

    @Mock
    private lateinit var walletDao: WalletDao

    @Mock
    private lateinit var appSettings: DataStore<AppSettings>
    private lateinit var assetsManager: AssetsManager

    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        appSettings = mock() {
            onBlocking { data } doReturn flow { emit(
                AppSettings(
                    ChainNetwork.MAIN,
                    Currency.getInstance(
                        Locale.US
                    ).let {
                        EasyCurrency(it.symbol, it.currencyCode)
                    })
            ) }
        }
        hdWallet = mock() {
            on { getAddressForCoin(CoinType.ETHEREUM) } doReturn "0xa4531dE99E22B2166d340E7221669DF565c52024"
        }
        walletDao = mock()

        val walletRepositoryImpl = WalletRepositoryImpl(walletDao)
        walletRepositoryImpl.inject(hdWallet)
        val response = File("./src/test/assets/mock_currencies.json").readBytes().decodeToString()
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(response),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                }
            }
        }
        assetsManager = AssetsManager(httpClient, appSettings, walletRepositoryImpl)
    }

    @Test
    fun `Mock HDWallet and test balance`() = runTest {
        val assetRepository = AssetRepositoryImpl(assetsManager)
        assetsManager.fetchAssets()
        assertEquals(BigInteger.ONE, assetRepository.balance("bitcoin", null))
    }

    @Test
    fun `Given mock response and test transactions failed`() = runTest {
        val assetRepositoryImpl = AssetRepositoryImpl(assetsManager)
        assetsManager.fetchAssets()
        assertFalse(assetRepositoryImpl.transactions("bitcoin", 0, 0, null).isSuccess)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}