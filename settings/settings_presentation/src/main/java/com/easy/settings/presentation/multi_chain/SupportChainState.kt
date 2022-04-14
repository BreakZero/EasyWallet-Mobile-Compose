package com.easy.settings.presentation.multi_chain

import com.easy.core.enums.ChainNetwork

data class SupportChainState(
    val selectedOne: ChainNetwork = ChainNetwork.MAIN,
    val supportNetworks: List<ChainNetwork>
)
