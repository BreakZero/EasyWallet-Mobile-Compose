package com.easy.settings.presentation.multi_chain

import com.easy.core.enums.Chain

data class SupportChainState(
    val checkId: Int = Chain.ETHEREUM.id,
    val supportChains: List<Chain>
) {
    fun selected(): Chain {
        return supportChains.find { it.id == checkId } ?: Chain.ETHEREUM
    }
}
