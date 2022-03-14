package com.easy.core.ui.common

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.easy.core.ui.R

object BiometricUtil {
    @RequiresApi(Build.VERSION_CODES.P)
    fun launchBiometric(
        context: Context,
        authenticationCallback: BiometricPrompt.AuthenticationCallback,
        onCancel: () -> Unit
    ) {
        if (checkBiometricSupport(context)) {
            val biometricPrompt = BiometricPrompt.Builder(context)
                .apply {
                    setTitle(context.getString(R.string.prompt_info_title))
                    setSubtitle(context.getString(R.string.prompt_info_subtitle))
                    setDescription(context.getString(R.string.prompt_info_description))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setConfirmationRequired(false)
                    }
                    setNegativeButton(
                        context.getString(R.string.prompt_info_use_app_password),
                        context.mainExecutor
                    ) { _, _ ->
                        onCancel.invoke()
                    }
                }.build()

            biometricPrompt.authenticate(
                CancellationSignal()
                    .apply {
                        setOnCancelListener {
                            onCancel.invoke()
                        }
                    },
                context.mainExecutor,
                authenticationCallback
            )
        }
    }

    private fun checkBiometricSupport(context: Context): Boolean {
        val keyGuardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyGuardManager.isDeviceSecure) {
            return true
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }
}