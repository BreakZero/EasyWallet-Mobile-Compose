package com.easy.assets.presentation.detail

import androidx.annotation.Keep
import com.easy.assets.domain.model.Transaction

@Keep
data class AssetDetailState(
    val isLoading: Boolean,
    val balance: Result<String>,
    val icon: String,
    val symbol: String,
    val result: Result<List<Transaction>>
)