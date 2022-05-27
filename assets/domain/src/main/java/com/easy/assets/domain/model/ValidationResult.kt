package com.easy.assets.domain.model

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
