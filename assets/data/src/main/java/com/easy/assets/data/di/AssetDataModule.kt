package com.easy.assets.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.easy.assets.data.AssetsManager
import com.easy.assets.data.model.local.AssetsDatabase
import com.easy.assets.data.model.remote.CallParameter
import com.easy.assets.data.model.remote.IntListParameter
import com.easy.assets.data.model.remote.Parameter
import com.easy.assets.data.model.remote.StringParameter
import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.assets.data.repository.CoinRepositoryImpl
import com.easy.assets.data.serializers.IntListParameterSerializer
import com.easy.assets.data.serializers.ParameterSerialize
import com.easy.assets.data.serializers.StringParameterSerializer
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.di.RPCHttpClient
import com.easy.core.model.AppSettings
import com.easy.wallets.repository.WalletRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AssetDataModule {
    @Binds
    abstract fun bindAssetRepository(
        impl: AssetRepositoryImpl
    ): AssetRepository

    @Binds
    abstract fun bindCoinRepository(
        impl: CoinRepositoryImpl
    ): CoinRepository

    companion object {
        @RPCHttpClient
        @Provides
        @Singleton
        fun ktorClient(): HttpClient {
            return HttpClient(Android) {
                defaultRequest {
                    header("Content-type", "application/json")
                    contentType(ContentType.Application.Json)
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            useArrayPolymorphism = true
                            prettyPrint = true
                            ignoreUnknownKeys = true
                            allowStructuredMapKeys = true
                            serializersModule = SerializersModule {
                                polymorphic(Parameter::class) {
                                    subclass(CallParameter::class, CallParameter.serializer())
                                    subclass(StringParameter::class, StringParameterSerializer)
                                    subclass(IntListParameter::class, IntListParameterSerializer)
                                }
                            }
                        }
                    )
                }
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("Easy-Http").d(message)
                        }
                    }
                    level = LogLevel.BODY
                }
                engine {
                    connectTimeout = 10_000
                }
            }
        }

        @Provides
        @Singleton
        fun provideChains(
            @RPCHttpClient ktorClient: HttpClient,
            appSettings: DataStore<AppSettings>,
            walletRepositoryImpl: WalletRepositoryImpl
        ): AssetsManager {
            return AssetsManager(ktorClient, appSettings, walletRepositoryImpl)
        }

        @Provides
        @Singleton
        fun provideAssetsDatabase(
            app: Application
        ): AssetsDatabase {
            return Room.databaseBuilder(app, AssetsDatabase::class.java, "easy_wallet_assets.db")
                .build()
        }
    }
}
