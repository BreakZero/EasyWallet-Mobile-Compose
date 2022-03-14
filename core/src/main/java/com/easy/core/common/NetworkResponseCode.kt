package com.easy.core.common

import io.ktor.client.features.*
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