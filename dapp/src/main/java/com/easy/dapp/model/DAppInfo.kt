package com.easy.dapp.model

import androidx.annotation.Keep

@Keep
data class DAppInfo(
    val url: String,
    val name: String,
    val icon: String
)
