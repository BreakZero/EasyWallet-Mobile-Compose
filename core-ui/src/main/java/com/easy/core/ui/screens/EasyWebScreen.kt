package com.easy.core.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.components.WebView
import com.easy.core.ui.components.rememberWebViewState
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EasyWebScreen(
    url: String,
    navigateUp: () -> Unit
) {
    val webViewState = rememberWebViewState(url = url)
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(navIcon = Icons.Filled.ArrowBack, backgroundColor = Color.White) {
                navigateUp()
            }
        }) {
        WebView(
            modifier = Modifier.fillMaxSize(),
            state = webViewState,
            onCreated = {
                it.settings.javaScriptEnabled = true
            }
        )
    }
}