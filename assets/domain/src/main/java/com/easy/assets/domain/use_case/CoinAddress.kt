package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.consts.NetworkChain
import javax.inject.Inject

class CoinAddress @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(chain: NetworkChain): String {
        return coinRepository.address(chain)
    }
}
