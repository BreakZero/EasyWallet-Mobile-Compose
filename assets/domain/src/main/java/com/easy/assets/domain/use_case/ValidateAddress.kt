package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.ValidationResult
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.consts.NetworkChain
import javax.inject.Inject

class ValidateAddress @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(chain: NetworkChain, address: String): ValidationResult {
        val coinType = coinRepository.coinType(chain)
        if (address.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The address can't be blank "
            )
        }
        if (!coinType.validate(address)) {
            return ValidationResult(
                successful = false,
                errorMessage = "The ${coinType.blockchain().name} address you entered is invalid. Make sure your address is correct and try again"
            )
        }
        return ValidationResult(successful = true)
    }
}