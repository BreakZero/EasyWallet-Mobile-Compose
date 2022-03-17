package com.easy.dapp.domain.model

import androidx.annotation.Keep

@Keep
data class DAppInfo(
    val url: String,
    val name: String,
    val icon: String,
    val chain: Int,
    val rpc: String
)
