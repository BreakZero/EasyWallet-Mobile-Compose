package com.easy.core.common

sealed class NetworkResponse<out T: Any> {
    data class Success<out T: Any>(val data: T): NetworkResponse<T>()

    data class Error(val code: Int): NetworkResponse<Nothing>()
}