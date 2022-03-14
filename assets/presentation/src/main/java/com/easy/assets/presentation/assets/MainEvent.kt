package com.easy.assets.presentation.assets

import com.easy.assets.domain.model.AssetInfo

sealed class MainUIEvent {
    object OnSettingsClick: MainUIEvent()
    object OnScanClick: MainUIEvent()
    object OnReceiveClick: MainUIEvent()
    object OnSendClick: MainUIEvent()
    data class OnItemClicked(val assetInfo: AssetInfo): MainUIEvent()
}