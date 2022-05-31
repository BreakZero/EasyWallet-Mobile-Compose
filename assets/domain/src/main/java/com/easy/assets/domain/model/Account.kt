package com.easy.assets.domain.model

import com.easy.core.consts.NetworkChain

data class Account(
    val chain: NetworkChain,
    val account: Int,
    val change: Int,
    val addressIndex: Int
)
