package com.easy.assets.domain.use_case.validation

import com.easy.assets.domain.model.ValidationResult

class ValidateAmount {
    fun execute(amount: String): ValidationResult {
        if (amount.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The amount can't be blank"
            )
        }
        if (!amount.matches("\\d+(\\.\\d+)?".toRegex())) {
            return ValidationResult(
                successful = false,
                errorMessage = "The amount is not a number"
            )
        }
        return ValidationResult(successful = true)
    }
}
