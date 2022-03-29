package com.easy.assets.presentation.assets

import androidx.annotation.Keep
import com.easy.assets.domain.model.AssetInfo

@Keep
data class AssetState(
    val tokenLists: Result<List<AssetInfo>> = Result.success(emptyList()),
    val isLoading: Boolean = false,
    val balances: HashMap<String, String> = hashMapOf()
)
