package com.easy.assets.data

import androidx.datastore.core.DataStore
import com.easy.assets.data.repository.CoinRepositoryImpl
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
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import java.io.File
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class CoinRepositoryTest {

    @Mock
    private lateinit var hdWallet: HDWallet

    @Mock
    private lateinit var walletDao: WalletDao
    private lateinit var appSettings: DataStore<AppSettings>
    private lateinit var assetsManager: AssetsManager

    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
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
        Dispatchers.setMain(testDispatcher)
        hdWallet = mock() {
            on { getAddressForCoin(CoinType.ETHEREUM) } doReturn "0xa4531dE99E22B2166d340E7221669DF565c52024"
        }
        walletDao = mock()

        val walletRepositoryImpl = WalletRepositoryImpl(walletDao)
        walletRepositoryImpl.inject(hdWallet)
        val response = File("./src/test/assets/mock_currencies.json").readBytes().decodeToString()
        val mockEngine = MockEngine { request ->
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
        assetsManager = AssetsManager(httpClient,appSettings, walletRepositoryImpl)
    }

    @Test
    fun `Mock HDWallet and check address for ethereum coin`() = runTest {
        val coinRepositoryImpl = CoinRepositoryImpl(assetsManager)
        assetsManager.fetchAssets()
        assertEquals(
            "0xa4531dE99E22B2166d340E7221669DF565c52024",
            coinRepositoryImpl.address("ethereum")
        )
    }

    @Test
    fun `Given mock response and test asset`() = runTest {
        val coinRepositoryImpl = CoinRepositoryImpl(assetsManager)
        assertEquals(7, coinRepositoryImpl.assets().size)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }
}