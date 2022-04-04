package com.easy.settings.presentation.currencies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val appSettings: DataStore<AppSettings>
) : ViewModel() {
    var currencyState by mutableStateOf(CurrencyState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.data.collect {
                currencyState = currencyState.copy(
                    selected = Currency.getInstance(it.localCurrency.code),
                    supportList = Currency.getAvailableCurrencies().toList()
                )
            }
        }
    }
}