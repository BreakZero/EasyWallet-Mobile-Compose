package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.ChainId
import java.math.BigInteger
import javax.inject.Inject

class AssetBalance @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(
        slug: String, contract: String?
    ): BigInteger {
        return repository.balance(slug = slug, contract = contract)
    }
}
