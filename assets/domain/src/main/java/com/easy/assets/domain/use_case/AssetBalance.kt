package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.NetworkChain
import java.math.BigInteger
import javax.inject.Inject

class AssetBalance @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(
        chain: NetworkChain,
        contract: String?
    ): BigInteger {
        return repository.balance(chain = chain, contract = contract)
    }
}
