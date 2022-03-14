package com.easy.core

import wallet.core.jni.HDWallet

object GlobalHolder {
    lateinit var hdWallet: HDWallet
        private set

    private val _assetCache = hashMapOf<String, Any>()

    fun inject(hdWallet: HDWallet) {
        this.hdWallet = hdWallet
    }


}