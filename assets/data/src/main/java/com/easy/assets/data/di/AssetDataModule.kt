package com.easy.assets.data.di

import com.easy.assets.data.repository.AssetRepositoryImpl
import com.easy.assets.domain.repository.AssetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AssetDataModule {

    @Binds
    abstract fun bindAssetRepository(
        impl: AssetRepositoryImpl
    ): AssetRepository
}