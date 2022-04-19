package com.easy.settings.presentation.ui

import com.easy.core.enums.ChainNetwork

data class SettingsState(
    val displayCurrency: String = "USD",
    val currentNetwork: ChainNetwork = ChainNetwork.MAIN
)
