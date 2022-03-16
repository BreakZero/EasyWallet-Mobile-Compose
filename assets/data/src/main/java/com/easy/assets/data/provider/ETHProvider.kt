package com.easy.assets.data.provider

import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.CallBalance
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.core.BuildConfig
import com.easy.core.ext.clearHexPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger

object ETHProvider {
    suspend fun balance(ktorClient: HttpClient, address: String, contract: String?): BigInteger {
        return withContext(Dispatchers.IO) {
            try {
                val reqBody = if (contract.isNullOrEmpty()) {
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
                                to = contract,
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
}