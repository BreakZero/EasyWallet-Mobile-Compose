package com.easy.assets.data.repository

import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.CallBalance
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.remote.dto.CoinConfigDto
import com.easy.assets.data.remote.dto.CoinConfigResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.BuildConfig
import com.easy.core.GlobalHolder
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.consts.ChainId
import com.easy.core.ext.byDecimal
import com.easy.core.ext.clearHexPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import wallet.core.jni.CoinType
import java.math.BigInteger
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val ktorClient: HttpClient
) : AssetRepository {
    private val _CoinConfigs = mutableListOf<CoinConfigDto>()

    override fun address(slug: String, legacy: Boolean): String {
        return when (slug) {
            "bitcoin" -> GlobalHolder.hdWallet.getAddressForCoin(CoinType.BITCOIN)
            else -> GlobalHolder.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
        }
    }

    override suspend fun signTransaction(slug: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun balance(
        address: String,
        chainId: ChainId,
        contractAddress: String?
    ): BigInteger {
        return withContext(Dispatchers.IO) {
            try {
                val reqBody = if (contractAddress.isNullOrEmpty()) {
                    BaseRpcRequest(
                        id = 1,
                        jsonrpc = "2.0",
                        method = "eth_getBalance",
                        params = listOf(address, "latest")
                    )
                } else {
                    BaseRpcRequest(
                        id = 1,
                        jsonrpc = "2.0",
                        method = "eth_call",
                        params = listOf(
                            CallBalance(
                                from = address,
                                to = contractAddress,
                                data = "0x70a08231000000000000000000000000${address.clearHexPrefix()}"
                            ), "latest"
                        )
                    )
                }
                val response = ktorClient
                    .post<HttpResponse>("https://mainnet.infura.io/v3/${BuildConfig.INFURA_APIKEY}") {
                        body = reqBody
                    }
                response.receive<BaseRpcResponseDto<String>>().result.clearHexPrefix()
                    .toBigInteger(16)
            } catch (e: Exception) {
                e.printStackTrace()
                BigInteger.ZERO
            }
        }
    }

    private suspend fun transactionsFromNetwork(
        address: String,
        chainId: ChainId,
        offset: Int,
        limit: Int,
        contractAddress: String?
    ): NetworkResponse<EthTxResponseDto> {
        val url = if (contractAddress.isNullOrEmpty()) {
            """
                https://api.etherscan.io/api?
                module=account
                &action=txlist
                &address=$address
                &page=1
                &offset=$offset
                &sort=desc
                &apikey=${BuildConfig.ETHERSCAN_APIKEY}
                """.trimIndent()
        } else {
            """
                https://api.etherscan.io/api?
                module=account
                &action=tokentx
                &contractaddress=$contractAddress
                &address=$address
                &page=1
                &offset=$offset
                &sort=desc
                &apikey=${BuildConfig.ETHERSCAN_APIKEY}
            """.trimIndent()
        }
        return try {
            val response = ktorClient.get<HttpResponse>(url)
            NetworkResponse.Success(response.receive())
        } catch (e: Throwable) {
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }

    override suspend fun transactions(address: String, chainId: ChainId, offset: Int, limit: Int, contractAddress: String?): Result<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            val networkTxs = transactionsFromNetwork(
                address = address,
                chainId = chainId,
                offset = offset,
                limit = limit,
                contractAddress = contractAddress
            )
            when (networkTxs) {
                is NetworkResponse.Success -> Result.success(networkTxs.data.result.map { it.toTransaction() })
                is NetworkResponse.Error -> Result.failure(IllegalArgumentException(networkTxs.code.toString()))
                else -> Result.failure(UnknownError())
            }
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

    override suspend fun assetsWithBalance(): Result<List<AssetInfo>> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                ktorClient.get<CoinConfigResponseDto>("http://45.153.130.249:8080/currencies")
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()?.result?.let { it ->
                _CoinConfigs.clear()
                _CoinConfigs.addAll(it)
                val balances = mutableListOf<Deferred<Pair<String, BigInteger>>>()
                coroutineScope {
                    for (item in it) {
                        val balance = async {
                            Pair(
                                item.coinSlug,
                                balance(
                                    address(item.coinSlug, false),
                                    ChainId.ETHEREUM,
                                    item.contractAddress.orEmpty()
                                )
                            )
                        }
                        balances.add(balance)
                    }
                }
                val balanceList = balances.awaitAll()
                Result.success(it.map { item ->
                    val sureBalance = balanceList.find { it.first == item.coinSlug }?.second
                        ?: BigInteger.ZERO
                    item.toAsset(sureBalance.byDecimal(item.decimal, 8))
                })
            } ?: Result.failure(Exception())
        }
    }
}