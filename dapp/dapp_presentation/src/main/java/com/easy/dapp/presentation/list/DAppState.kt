package com.easy.dapp.presentation.list

import androidx.annotation.Keep
import com.easy.dapp.domain.model.DAppInfo

@Keep
data class DAppState(
    val dApps: Result<List<DAppInfo>>,
    val isLoading: Boolean
)
