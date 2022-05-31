package com.easy.core.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.components.WebView
import com.easy.core.ui.components.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EasyWebScreen(
    url: String,
    navigateUp: () -> Unit
) {
    val webViewState = rememberWebViewState(url = url)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EasyAppBar {
                navigateUp()
            }
        }
    ) {
        WebView(
            modifier = Modifier.fillMaxSize(),
            state = webViewState,
            onCreated = {
                it.settings.javaScriptEnabled = true
            }
        )
    }
}
