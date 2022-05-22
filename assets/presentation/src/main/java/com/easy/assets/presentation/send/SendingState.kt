package com.easy.assets.presentation.send

import com.easy.assets.domain.model.AssetInfo

data class SendingState(
    val assetInfo: AssetInfo? = null,
    val toAddress: String = "",
    val amount: String = "",
    val fee: String? = null,
    val memo: String? = null,
    val action: Action = Action.ADVANCED
)

enum class Action {
    ADVANCED, MODEL_INFO
}
