package com.easy.assets.presentation.send

import com.easy.assets.domain.model.AssetInfo

data class SendFlowState(
    val assetInfo: AssetInfo? = null,
    val toAddress: String? = null,
    val amount: String? = null
)
