package com.easy.core

import wallet.core.jni.HDWallet

object GlobalHolder {
    lateinit var hdWallet: HDWallet
        private set

    fun inject(hdWallet: HDWallet) {
        this.hdWallet = hdWallet
    }
}