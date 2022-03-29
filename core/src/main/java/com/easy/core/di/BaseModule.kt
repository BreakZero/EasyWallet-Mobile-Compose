package com.easy.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {
    @Provides
    @Singleton
    fun ktorClient(): HttpClient {
        return HttpClient(Android) {
            install(DefaultRequest) {
                header("Content-type", "application/json")
                contentType(ContentType.Application.Json)
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            engine {
                connectTimeout = 10_000
            }
        }
    }
}