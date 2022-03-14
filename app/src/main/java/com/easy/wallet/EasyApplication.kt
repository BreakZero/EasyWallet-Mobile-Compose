package com.easy.wallet

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger

@HiltAndroidApp
class EasyApplication: Application() {
    init {
        System.loadLibrary("TrustWalletCore")
    }
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this)
        AndroidThreeTen.init(this)
    }
}