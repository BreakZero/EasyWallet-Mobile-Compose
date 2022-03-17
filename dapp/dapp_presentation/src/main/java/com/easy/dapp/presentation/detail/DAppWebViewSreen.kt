package com.easy.dapp.presentation.detail

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.easy.core.ui.components.EasyAppBar
import com.easy.dapp.presentation.common.WebAppInterface
import com.easy.dapp.presentation.components.DAppWebView
import com.easy.dapp.presentation.components.rememberWebViewState
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import logcat.logcat

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DAppWebViewScreen(
    url: String,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                backgroundColor = Color.White,
                title = "DAPP"
            ) {
                navigateUp.invoke()
            }
        }
    ) {
        val state = rememberWebViewState(url = url)

        DAppWebView(state = state,
            modifier = Modifier.fillMaxSize(),
            onCreated = {
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.addJavascriptInterface(WebAppInterface(context, it, url) { title, message ->
                    logcat("===== ") { "title: $title, message: $message" }
                }, "_tw_")
            })
    }
}