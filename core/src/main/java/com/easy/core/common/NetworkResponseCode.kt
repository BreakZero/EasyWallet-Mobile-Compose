package com.easy.core.common

import io.ktor.client.plugins.*
import java.nio.channels.UnresolvedAddressException

object NetworkResponseCode {
    fun checkError(e: Throwable): Int {
        return when(e) {
            is RedirectResponseException -> e.response.status.value
            is ClientRequestException -> e.response.status.value
            is ServerResponseException -> e.response.status.value
            is UnresolvedAddressException -> -1
            else -> -2
        }
    }
}