package com.easy.core.common

import androidx.annotation.Keep

@Keep
data class Navigator(
    val router: String
) {
    private val params: MutableList<KeyPair> = mutableListOf()
    fun add(lambda: () -> Pair<String, String?>) {
        with(lambda()) {
            params.add(KeyPair(first, second))
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

fun navigator(router: String, lambda: Navigator.() -> Unit): Navigator {
    val navigator = Navigator(router)
    navigator.apply(lambda)
    return navigator
}

private data class KeyPair(
    val type: String,
    val value: String?
)