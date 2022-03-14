package com.easy.assets.presentation.assets

import androidx.annotation.Keep
import com.easy.assets.domain.model.AssetInfo

@Keep
data class AssetState(
    val tokenResult: Result<Map<String,List<AssetInfo>>> = Result.success(mapOf()),
    val isLoading: Boolean = false,
    val balances: HashMap<String, String> = hashMapOf()
)
