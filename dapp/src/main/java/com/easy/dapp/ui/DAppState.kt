package com.easy.dapp.ui

import androidx.annotation.Keep
import com.easy.dapp.model.DAppInfo

@Keep
data class DAppState(
    val dApps: Result<List<DAppInfo>>,
    val isLoading: Boolean
)
