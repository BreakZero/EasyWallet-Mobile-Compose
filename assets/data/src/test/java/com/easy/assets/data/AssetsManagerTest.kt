package com.easy.assets.data

import androidx.datastore.core.DataStore
import com.easy.assets.data.provider.DefaultChain
import com.easy.core.consts.NetworkChain
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class AssetsManagerTest {
    private lateinit var httpClient: HttpClient
    private lateinit var walletDao: WalletDao
    private lateinit var appSettings: DataStore<AppSettings>
    private lateinit var assetManager: AssetsManager

    @Before
    fun setUp() {
        walletDao = mock()
        appSettings = mock() {
            on { data } doReturn flow {
                emit(
                    AppSettings(
                        ChainNetwork.MAIN,
                        Currency.getInstance(
                            Locale.US
                        ).let {
                            EasyCurrency(it.symbol, it.currencyCode)
                        })
                )
            }
        }
        val response = File("./src/test/assets/mock_currencies.json").readBytes().decodeToString()
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(response),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                }
            }
        }
        assetManager = AssetsManager(httpClient, appSettings, WalletRepositoryImpl(walletDao))
    }

    @Test
    fun `Given mock response and test assets result`() = runTest {
        assertEquals(7, assetManager.fetchAssets().size)
    }

    @Test
    fun `Given asserts and check find method`() = runTest {
        assetManager.fetchAssets()
        assertNotEquals(DefaultChain, assetManager.find(NetworkChain.Bitcoin))
        assertEquals(DefaultChain, assetManager.find(NetworkChain.Ethereum))
    }

    @Test
    fun `Given an error response check`() = runTest {
        val mockEngine = MockEngine { _ ->
            respondBadRequest()
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                }
            }
        }
        val manager = AssetsManager(httpClient, appSettings, WalletRepositoryImpl(walletDao))
        assertEquals(1, manager.fetchAssets().size)
    }
}