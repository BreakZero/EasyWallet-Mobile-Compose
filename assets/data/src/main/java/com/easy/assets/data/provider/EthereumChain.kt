package com.easy.assets.data.provider

import com.easy.assets.data.remote.BaseRpcRequest
import com.easy.assets.data.remote.CallBalance
import com.easy.assets.data.remote.dto.BaseRpcResponseDto
import com.easy.assets.data.remote.dto.EthTxResponseDto
import com.easy.assets.domain.model.TransactionPlan
import com.easy.core.BuildConfig
import com.easy.core.GlobalHolder
import com.easy.core.common.NetworkResponse
import com.easy.core.common.NetworkResponseCode
import com.easy.core.ext.clearHexPrefix
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import wallet.core.jni.CoinType
import java.math.BigInteger

internal class EthereumChain(
    private val ktorClient: HttpClient
) : IChain {
    override suspend fun sign(plan: TransactionPlan): String {
        TODO("Not yet implemented")
    }

    override fun address(): String {
        return GlobalHolder.hdWallet.getAddressForCoin(CoinType.ETHEREUM)
    }

    override suspend fun balance(contract: String?): BigInteger {
        return try {
            val reqBody = if (contract.isNullOrEmpty()) {
                BaseRpcRequest(
                    id = 1,
                    jsonrpc = "2.0",
                    method = "eth_getBalance",
                    params = listOf(address(), "latest")
                )
            } else {
                BaseRpcRequest(
                    id = 1,
                    jsonrpc = "2.0",
                    method = "eth_call",
                    params = listOf(
                        CallBalance(
                            from = address(),
                            to = contract,
                            data = "0x70a08231000000000000000000000000${address().clearHexPrefix()}"
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

    override suspend fun transactions(
        offset: Int,
        limit: Int,
        contract: String?
    ): NetworkResponse<EthTxResponseDto> {
        val url = if (contract.isNullOrEmpty()) {
            """
                https://api.etherscan.io/api?
                module=account
                &action=txlist
                &address=${address()}
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
                &contractaddress=$contract
                &address=${address()}
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
}