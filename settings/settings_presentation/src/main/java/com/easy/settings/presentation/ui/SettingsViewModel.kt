package com.easy.settings.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettings: DataStore<AppSettings>
) : ViewModel() {
    var settingsState by mutableStateOf(SettingsState())

    init {
        viewModelScope.launch {
            appSettings.data.collect {
                settingsState = settingsState.copy(
                    displayCurrency = it.localCurrency.code,
                    currentNetwork = it.chain.name
                )
            }
        }
    }
}