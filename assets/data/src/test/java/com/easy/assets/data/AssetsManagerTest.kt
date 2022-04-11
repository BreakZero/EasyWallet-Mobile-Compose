package com.easy.assets.data

import com.easy.assets.data.provider.DefaultChain
import com.easy.wallets.data.WalletDao
import com.easy.wallets.repository.WalletRepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class AssetsManagerTest {
    private lateinit var httpClient: HttpClient
    private lateinit var walletDao: WalletDao
    private lateinit var assetManager: AssetsManager

    @Before
    fun setUp() {
        walletDao = mock()
        val response = File("./src/test/assets/mock_currencies.json").readBytes().decodeToString()
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(response),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        httpClient = HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
        assetManager = AssetsManager(httpClient, WalletRepositoryImpl(walletDao))
    }

    @Test
    fun `Given mock response and test assets result`() = runTest {
        assertEquals(7, assetManager.fetchAssets().size)
    }

    @Test
    fun `Given asserts and check find method`() = runTest {
        assetManager.fetchAssets()
        assertNotEquals(DefaultChain, assetManager.find("erc20-uni"))
        assertEquals(DefaultChain, assetManager.find("erc20-uni1"))
    }

    @Test
    fun `Given an error response check`() = runTest {
        val mockEngine = MockEngine { request ->
            respondBadRequest()
        }
        val manager = AssetsManager(HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }, WalletRepositoryImpl(walletDao))
        assertEquals(1, manager.fetchAssets().size)
    }
}