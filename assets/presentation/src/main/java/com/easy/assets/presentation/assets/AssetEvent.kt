package com.easy.assets.presentation.assets

import com.easy.assets.domain.model.AssetInfo

sealed class AssetEvent {
    object SwipeToRefresh : AssetEvent()
    object SettingsClicked: AssetEvent()
    object ScanClicked: AssetEvent()
    data class ItemClicked(val assetInfo: AssetInfo) : AssetEvent()
}
