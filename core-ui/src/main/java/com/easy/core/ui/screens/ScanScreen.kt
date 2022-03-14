package com.easy.core.ui.screens

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.easy.core.ui.components.ScannerView

@Composable
fun ScanScreen(
    callback: () -> Unit
) {
    Surface {
        ScannerView(callback = {
            callback.invoke()
        })
    }
}