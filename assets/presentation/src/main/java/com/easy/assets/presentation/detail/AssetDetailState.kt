package com.easy.assets.presentation.detail

import androidx.annotation.Keep
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction

@Keep
data class AssetDetailState(
    val isLoading: Boolean,
    val balance: Result<String>,
    val assetInfo: AssetInfo? = null,
    val transactions: Result<List<Transaction>>
)