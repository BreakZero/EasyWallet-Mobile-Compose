package com.easy.assets.data.di

import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.assets.domain.repository.AssetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AssetDataModule {
    @Singleton
    @Provides
    fun provideAssetRepository(
        ktorClient: HttpClient
    ): AssetRepository {
        return AssetRepositoryImpl(ktorClient)
    }
}