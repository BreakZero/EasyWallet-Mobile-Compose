package com.easy.dapp.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DAppWebView(
  url: String
) {
  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
  ) {
    val state = rememberWebViewState(url = url)
    WebView(state = state,
      onCreated = {
        it.settings.javaScriptEnabled = true
      })
  }
}