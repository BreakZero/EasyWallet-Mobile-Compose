package com.easy.settings.presentation.currencies

import java.util.*

sealed class CurrencyEvent {
    object OnDone : CurrencyEvent()
    data class OnSelected(val currency: Currency) : CurrencyEvent()
}
