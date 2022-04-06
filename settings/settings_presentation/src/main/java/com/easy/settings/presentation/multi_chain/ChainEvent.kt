package com.easy.settings.presentation.multi_chain

sealed class ChainEvent {
    data class OnSelected(val selectedId: Int): ChainEvent()
    object OnDone: ChainEvent()
}
