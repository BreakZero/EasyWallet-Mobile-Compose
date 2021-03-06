package com.easy.core.common

import androidx.annotation.Keep

@Keep
data class Navigator(
    val router: String
) {
    private val params: MutableList<KeyPair> = mutableListOf()

    internal fun addParameter(type: String, value: String?) {
        params.add(KeyPair(type, value))
    }

    fun routerWithParameter(): String {
        return params.let {
            buildString {
                append(router)
                append("?")
                it.forEach {
                    append(it.key)
                    append("=")
                    append(it.value)
                    append("&")
                }
            }.removeSuffix("&")
        }
    }

    fun router(): String {
        return params.let {
            // /${info.contractAddress}/${info.symbol}
            buildString {
                append(router)
                it.forEach {
                    append("/")
                    append(it.value)
                }
            }
        }
    }
}

fun Navigator(router: String, init: Navigator.() -> Unit): Navigator {
    return Navigator(router).apply(init)
}

class _Parameter(private val navigator: Navigator) {
    infix fun String.to(that: String?) = navigator.addParameter(this, that)
}

inline fun Navigator.parameter(block: _Parameter.() -> Unit) {
    _Parameter(this).apply(block)
}

private data class KeyPair(
    val key: String,
    val value: Any?
)
