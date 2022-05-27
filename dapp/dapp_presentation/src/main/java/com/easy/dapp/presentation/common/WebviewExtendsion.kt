package com.easy.dapp.presentation.common

import android.webkit.WebView

fun WebView.sendError(methodId: Long, message: String) {
    val script = "window.ethereum.sendError($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}

fun WebView.sendResult(methodId: Long, message: String) {
    val script = "window.ethereum.sendResponse($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}

fun WebView.sendResults(methodId: Long, messages: List<String>) {
    val message = messages.joinToString(separator = ",")
    val script = "window.ethereum.sendResponse($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}