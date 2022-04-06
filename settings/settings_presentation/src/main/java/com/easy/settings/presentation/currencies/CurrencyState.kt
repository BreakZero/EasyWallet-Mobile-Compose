package com.easy.settings.presentation.currencies

import androidx.annotation.Keep
import java.util.*

@Keep
data class CurrencyState(
    val selected: Currency = Currency.getInstance(Locale.US),
    val supportList: List<Currency> = emptyList()
) {
    fun selected(): Currency {
        return supportList.find { it.currencyCode == selected.currencyCode }
            ?: Currency.getInstance(Locale.US)
    }
}