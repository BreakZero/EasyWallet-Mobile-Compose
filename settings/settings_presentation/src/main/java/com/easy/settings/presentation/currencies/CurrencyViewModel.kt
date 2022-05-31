package com.easy.settings.presentation.currencies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.model.AppSettings
import com.easy.core.model.EasyCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val appSettings: DataStore<AppSettings>
) : ViewModel() {
    var currencyState by mutableStateOf(CurrencyState())
    private val _uiEvent = Channel<CurrencyUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.data.collect {
                withContext(Dispatchers.Main) {
                    currencyState = currencyState.copy(
                        selected = Currency.getInstance(it.localCurrency.code),
                        supportList = listOf(
                            Currency.getInstance(Locale.US),
                            Currency.getInstance(Locale.CHINA),
                            Currency.getInstance(Locale.JAPAN),
                            Currency.getInstance("HKD"),
                            Currency.getInstance("GBP")
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: CurrencyEvent) {
        when (event) {
            is CurrencyEvent.OnDone -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateCurrency()
                    _uiEvent.send(CurrencyUIEvent.ActionDone)
                }
            }
            is CurrencyEvent.OnSelected -> {
                currencyState = currencyState.copy(selected = event.currency)
            }
        }
    }

    private suspend fun updateCurrency() {
        appSettings.updateData { appSettings ->
            appSettings.copy(
                localCurrency = currencyState.selected().let {
                    EasyCurrency(symbol = it.symbol, code = it.currencyCode)
                }
            )
        }
    }
}
