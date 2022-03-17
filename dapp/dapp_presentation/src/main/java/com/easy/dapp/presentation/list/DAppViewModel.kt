package com.easy.dapp.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DAppViewModel @Inject constructor(): ViewModel() {
    var dAppState by mutableStateOf(DAppState(dApps = Result.success(emptyList()), isLoading = true))

    init {
        dAppState = dAppState.copy(dApps = Result.success(listOf()), isLoading = false)
    }
}