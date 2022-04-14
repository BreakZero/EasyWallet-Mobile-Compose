package com.easy.settings.presentation.multi_chain

import com.easy.core.enums.ChainNetwork

sealed class ChainEvent {
    data class OnSelected(val network: ChainNetwork): ChainEvent()
    object OnDone: ChainEvent()
}
