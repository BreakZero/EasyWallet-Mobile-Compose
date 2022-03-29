package com.easy.assets.data.di

import com.easy.assets.data.AssetsManager
import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.assets.data.repository.CoinRepositoryImpl
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
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
            ktorClient: HttpClient
        ): AssetsManager {
            return AssetsManager(ktorClient)
        }
    }
}