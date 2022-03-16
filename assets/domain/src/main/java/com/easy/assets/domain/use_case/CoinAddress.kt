package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.CoinRepository
import javax.inject.Inject

class CoinAddress @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(slug: String): String {
        return coinRepository.address(slug)
    }
}
