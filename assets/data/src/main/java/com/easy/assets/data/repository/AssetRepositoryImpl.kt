package com.easy.assets.data.repository

import com.easy.assets.data.mapper.toTransaction
import com.easy.assets.data.provider.ETHProvider
import com.easy.assets.data.remote.dto.EthTxResponseDto
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
    override suspend fun signTransaction(slug: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun balance(
        address: String,
        chainId: ChainId,
        contractAddress: String?
    ): BigInteger {
        return ETHProvider.balance(ktorClient, address, contractAddress)
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
                is NetworkResponse.Success -> Result.success(networkTxs.data.result.map { it.toTransaction(address) })
                is NetworkResponse.Error -> Result.failure(IllegalArgumentException(networkTxs.code.toString()))
                else -> Result.failure(UnknownError())
            }
        }
    }
}