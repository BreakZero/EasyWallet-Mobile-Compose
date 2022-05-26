package com.easy.dapp.presentation.common

data class MessageInfo(
    val title: String,
    val methodId: Long,
    val data: String,
    val method: DAppMethod
)
