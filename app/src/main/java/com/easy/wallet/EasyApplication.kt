package com.easy.wallet

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class EasyApplication : Application() {
    init {
        System.loadLibrary("TrustWalletCore")
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
