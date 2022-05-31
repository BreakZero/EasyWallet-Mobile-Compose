package com.easy.core.common

sealed class UiEvent {
    object Success : UiEvent()
    object NavigateUp : UiEvent()
    data class NavigateTo(val navigator: Navigator) : UiEvent()
    data class ShowSnackbar(val message: UiText) : UiEvent()
}
