package com.easy.dapp.presentation.common

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.easy.core.common.hex
import com.easy.core.ext.toHexBytes
import org.json.JSONObject
import wallet.core.jni.Curve
import wallet.core.jni.PrivateKey

class WebAppInterface(
    private val context: Context,
    private val webView: WebView,
    private val dappUrl: String,
    val callback: (String, String) -> Unit
) {
    private val privateKey =
        PrivateKey("0x4646464646464646464646464646464646464646464646464646464646464646".toHexBytes())
    private val addr = "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5"

    @JavascriptInterface
    fun postMessage(json: String) {
        val obj = JSONObject(json)
        val id = obj.getLong("id")
        when (val method = DAppMethod.fromValue(obj.getString("name"))) {
            DAppMethod.REQUESTACCOUNTS -> {
                callback.invoke(
                    "Request Accounts",
                    "DApp(${dappUrl}) need to get your address"
                )
                val setAddress = "window.ethereum.setAddress(\"$addr\");"
                val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
                webView.post {
                    webView.evaluateJavascript(setAddress) {
                        // ignore
                    }
                    webView.evaluateJavascript(callback) { _ ->
                    }
                }
            }
            DAppMethod.SIGNTRANSACTION -> {
                val data = extractMessage(obj)
                Toast.makeText(context, data.hex, Toast.LENGTH_SHORT).show()
            }
            DAppMethod.SIGNMESSAGE -> {
                val data = extractMessage(obj)
                handleSignMessage(id, data, addPrefix = false)
            }
            DAppMethod.SIGNPERSONALMESSAGE -> {
                val data = extractMessage(obj)
                handleSignMessage(id, data, addPrefix = true)
            }
            DAppMethod.SIGNTYPEDMESSAGE -> {
                val data = extractMessage(obj)
                val raw = extractRaw(obj)
                handleSignTypedMessage(id, data, raw)
            }
            else -> {
                callback.invoke(
                    "Error",
                    "$method not implemented"
                )
            }
        }
    }

    private fun extractMessage(json: JSONObject): ByteArray {
        val param = json.getJSONObject("object")
        val data = param.getString("data")
        return data.toHexBytes()
    }

    private fun extractRaw(json: JSONObject): String {
        val param = json.getJSONObject("object")
        return param.getString("raw")
    }

    private fun handleSignMessage(id: Long, data: ByteArray, addPrefix: Boolean) {
    }

    private fun handleSignTypedMessage(id: Long, data: ByteArray, raw: String) {
        webView.sendResult(signEthereumMessage(data, false), id)
    }

    private fun signEthereumMessage(message: ByteArray, addPrefix: Boolean): String {
        var data = message
        if (addPrefix) {
            val messagePrefix = "\u0019Ethereum Signed Message:\n"
            val prefix = (messagePrefix + message.size).toByteArray()
            val result = ByteArray(prefix.size + message.size)
            System.arraycopy(prefix, 0, result, 0, prefix.size)
            System.arraycopy(message, 0, result, prefix.size, message.size)
            data = wallet.core.jni.Hash.keccak256(result)
        }

        val signatureData = privateKey.sign(data, Curve.SECP256K1)
            .apply {
                (this[this.size - 1]) = (this[this.size - 1] + 27).toByte()
            }
        return signatureData.hex
    }
}
