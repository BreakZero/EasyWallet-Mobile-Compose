@file:Suppress("BlockingMethodInNonBlockingContext")

package com.easy.core.model

import androidx.datastore.core.Serializer
import com.easy.core.enums.ChainNetwork
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@Serializable
data class AppSettings(
    val network: ChainNetwork,
    val localCurrency: EasyCurrency
)

@Serializable
data class EasyCurrency(
    val symbol: String,
    val code: String
)

object AppSettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings
        get() = AppSettings(
            network = ChainNetwork.MAIN,
            localCurrency = Currency.getInstance(Locale.US).let {
                EasyCurrency(it.symbol, it.currencyCode)
            }
        )

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}
