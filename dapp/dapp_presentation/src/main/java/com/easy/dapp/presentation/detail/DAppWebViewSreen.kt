package com.easy.dapp.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.easy.core.BuildConfig
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.components.WebView
import com.easy.core.ui.components.rememberWebViewState
import com.easy.dapp.presentation.R
import com.easy.dapp.presentation.common.WebAppInterface
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import logcat.logcat

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DAppWebViewScreen(
    url: String,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val providerJs = remember {
        context.resources.openRawResource(R.raw.trust_min_new).readBytes().decodeToString()
    }

    val initJs = remember {
        """
        (function() {
            var config = {
                chainId: 1,
                rpcUrl: "https://mainnet.infura.io/v3/${BuildConfig.INFURA_APIKEY}",
                isDebug: true
            };
            window.ethereum = new trustwallet.Provider(config);
            window.web3 = new trustwallet.Web3(window.ethereum);
            trustwallet.postMessage = (json) => {
                window._tw_.postMessage(JSON.stringify(json));
            }
        })();
        """
    }
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
        val scope = rememberCoroutineScope()

        var showDialog by remember {
            mutableStateOf(false)
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = {})
                    { Text(text = "OK") }
                },
                dismissButton = {
                    TextButton(onClick = {

                    })
                    { Text(text = "Cancel") }
                },
                title = { Text(text = "Please confirm") },
                text = { Text(text = "Should I continue with the requested action?") }
            )
        }

        WebView(state = state,
            modifier = Modifier.fillMaxSize(),
            startScript = {
                evaluateJavascript(providerJs, null)
                evaluateJavascript(initJs, null)
                val script = "window.ethereum.request({method: \"eth_requestAccounts\"})"
                evaluateJavascript(script) {
                    logcat { "======== $it" }
                }
            },
            onCreated = {
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.addJavascriptInterface(WebAppInterface(context, it, url) { title, message ->
                    scope.launch {
                        showDialog = true
                    }
                }, "_tw_")
            })
    }
}