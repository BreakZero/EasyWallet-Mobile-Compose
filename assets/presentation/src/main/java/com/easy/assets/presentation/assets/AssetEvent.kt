package com.easy.assets.presentation.assets

import com.easy.assets.domain.model.AssetInfo

sealed class AssetEvent {
    object OnRefresh : AssetEvent()
    object OnReceive : AssetEvent()
    object OnSend : AssetEvent()
    data class OnItemClick(val assetInfo: AssetInfo) : AssetEvent()
}

sealed class AssetUIEvent {
    data class OnItemClick(val assetInfo: AssetInfo): AssetUIEvent()
    object OnReceiveClick: AssetUIEvent()
    object OnSendClick: AssetUIEvent()
}