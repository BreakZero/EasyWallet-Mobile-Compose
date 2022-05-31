package com.easy.assets.presentation.detail

import androidx.annotation.Keep
import androidx.paging.Pager
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.model.Transaction

@Keep
data class AssetDetailState(
    val balance: Result<String>,
    val assetInfo: AssetInfo? = null,
    val pager: Pager<Int, Transaction>? = null
)
