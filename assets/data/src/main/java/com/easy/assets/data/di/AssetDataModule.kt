package com.easy.assets.data.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.room.Room
import com.easy.assets.data.AssetsManager
import com.easy.assets.data.model.local.AssetsDatabase
import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.assets.data.repository.CoinRepositoryImpl
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
import com.easy.core.model.AppSettings
import com.easy.wallets.repository.WalletRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
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
        @Provides
        @Singleton
        fun provideChains(
            ktorClient: HttpClient,
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
