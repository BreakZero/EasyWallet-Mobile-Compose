package com.easy.intro.walletimport

sealed class ImportEvent {
    object OnImportClick : ImportEvent()
    data class OnPhraseChange(val phrase: String) : ImportEvent()
    data class OnFocusChange(val isFocused: Boolean): ImportEvent()
}