package com.easy.dapp.presentation.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.EasyAppBar
import com.easy.dapp.presentation.R
import com.easy.dapp.presentation.common.DAppMethod
import com.easy.dapp.presentation.common.WebAppInterface
import com.easy.dapp.presentation.common.sendError
import com.easy.dapp.presentation.common.sendResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DAppWebViewScreen(
    url: String,
    chain: Int,
    rpc: String,
    dappViewModel: DAppDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    // do not support right now. when recompose, will reset to null here
    val providerJs = remember {
        context.resources.openRawResource(R.raw.trust_min).readBytes().decodeToString()
    }

    val initJs = remember {
        """
        (function() {
            var config = {
                chainId: $chain,
                rpcUrl: "$rpc",
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
            .fillMaxSize(),
        topBar = {
            EasyAppBar(
                title = "DApp"
            ) {
                navigateUp.invoke()
            }
        }
    ) {
        val uiState = dappViewModel.dAppDetailState
        val state = rememberWebViewState(url = url)
        val scope = rememberCoroutineScope()

        val systemUIController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val statusColor = MaterialTheme.colorScheme.surface
        LaunchedEffect(key1 = null) {
            systemUIController.setStatusBarColor(color = statusColor, darkIcons = useDarkIcons)
        }

        if (uiState.showDialog) {
            uiState.messageInfo?.let {
                AlertDialog(onDismissRequest = {}, confirmButton = {
                    TextButton(onClick = {
                        if (it.method == DAppMethod.REQUESTACCOUNTS) {
                            Timber.tag("Easy").d("webview: ${dappViewModel.webView}")
                            val setAddress =
                                "window.ethereum.setAddress(\"0x81080a7e991bcdddba8c2302a70f45d6bd369ab5\");"
                            val sendAddress = "window.ethereum.sendResponse(${it.methodId}, [\"0x81080a7e991bcdddba8c2302a70f45d6bd369ab5\"])"
                            dappViewModel.webView?.post {
                                dappViewModel.webView?.evaluateJavascript(setAddress) {
                                    // ignore
                                }
                                dappViewModel.webView?.evaluateJavascript(sendAddress) { _ ->
                                }
                            }
                        } else {
                            dappViewModel.webView?.sendResult(it.methodId, "")
                        }
                        dappViewModel.approve()
                    }) { Text(text = "Approve") }
                }, dismissButton = {
                        TextButton(onClick = {
                            dappViewModel.reject()
                            dappViewModel.webView?.sendError(it.methodId, "User reject the action")
                        }) { Text(text = "Reject") }
                    }, title = { Text(text = it.title) }, text = { Text(text = it.data) })
            }
        }

        val webClient = remember {
            object : AccompanistWebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    view?.also { webView ->
                        webView.evaluateJavascript(providerJs, null)
                        webView.evaluateJavascript(initJs, null)
                    }
                }
            }
        }
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            client = webClient,
            onCreated = { webView ->
                dappViewModel.injectWebView(webView)
                webView.settings.javaScriptEnabled = true
                webView.settings.domStorageEnabled = true
                webView.addJavascriptInterface(
                    WebAppInterface(webView, url) {
                        scope.launch {
                            dappViewModel.onReceiveMethod(it)
                        }
                    },
                    "_tw_"
                )
                val script = "window.ethereum.request({method: \"eth_requestAccounts\"})"
                webView.evaluateJavascript(script) { }
            }
        )
    }
}
