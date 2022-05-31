package com.easy.dapp.presentation.detail

import com.easy.dapp.presentation.common.MessageInfo

data class DAppDetailState(
    val appName: String = "DAPP",
    val data: String? = null,
    val showDialog: Boolean = false,
    val messageInfo: MessageInfo? = null
)
