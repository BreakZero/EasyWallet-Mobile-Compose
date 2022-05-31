package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.ext.byDecimal
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger
import javax.inject.Inject

class AssetsWithBalance @Inject constructor(
    private val coinRepository: CoinRepository,
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(): Result<List<AssetInfo>> {
        return coinRepository.assets().let {
            val balances = mutableListOf<Deferred<Pair<String, BigInteger>>>()
            coroutineScope {
                for (item in it) {
                    val balance = async {
                        Pair(
                            item.slug,
                            assetRepository.balance(
                                item.chain,
                                item.contractAddress
                            )
                        )
                    }
                    balances.add(balance)
                }
            }
            val balanceList = balances.awaitAll()
            Result.success(it.map { item ->
                val sureBalance = balanceList.find { it.first == item.slug }?.second
                    ?: BigInteger.ZERO
                item.copy(balance = sureBalance.byDecimal(item.decimal, 8))
            })
        }
    }
}