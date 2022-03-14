package com.easy.assets.data.repository

import com.easy.assets.data.mapper.toAsset
import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.data.remote.dto.CoinConfigResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.BuildConfig
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.consts.ChainId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import javax.inject.Inject

class AssetRepositoryImpl @Inject constructor(
    private val ktorClient: HttpClient
) : AssetRepository {
    override fun address(legacy: Boolean): String {
        return "mock address"
    }

    override suspend fun signTransaction(): String {
        TODO("Not yet implemented")
    }

    override suspend fun balance(address: String, chainId: ChainId, contractAddress: String): BigInteger {
        return BigInteger.ZERO
    }

    private suspend fun transactionsFromNetwork(
        address: String,
        chainId: ChainId,
        offset: Int,
        limit: Int,
        contractAddress: String
    ): NetworkResponse<EthTxResponseDto> {
        return try {
            val response = ktorClient.get<HttpResponse>(
                """
                https://api.etherscan.io/api?
                module=account
                &action=txlist
                &address=$address
                &page=0
                &offset=$offset
                &sort=desc
                &apikey=${BuildConfig.ETHERSCAN_APIKEY}
                """.trimIndent()
            )
            NetworkResponse.Success(response.receive())
        } catch (e: Throwable) {
            NetworkResponse.Error(NetworkResponseCode.checkError(e))
        }
    }

    override suspend fun transactions(address: String, chainId: ChainId, offset: Int, limit: Int, contractAddress: String): Result<List<Transaction>> {
        val networkTxs = transactionsFromNetwork(
            address = address,
            chainId = chainId,
            offset = offset,
            limit = limit,
            contractAddress = contractAddress
        )
        return when(networkTxs) {
            is NetworkResponse.Success -> Result.success(networkTxs.data.result.map { it.toTransaction() })
            is NetworkResponse.Error -> Result.failure(IllegalArgumentException(networkTxs.code.toString()))
            else -> Result.failure(UnknownError())
        }
    }

    override suspend fun assets(): Result<Map<String, List<AssetInfo>>> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                ktorClient.get<CoinConfigResponseDto>("http://45.153.130.249:8080/currencies")
            }.onFailure {
                it.printStackTrace()
            }.getOrNull()?.let { it ->
                Result.success(it.result.map {
                    it.toAsset()
                }.groupBy { it.symbol })
            } ?: Result.failure(Exception())
        }
    }
}