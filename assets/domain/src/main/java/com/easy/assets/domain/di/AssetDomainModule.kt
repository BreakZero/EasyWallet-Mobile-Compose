package com.easy.assets.domain.di

import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AssetDomainModule {

    @Provides
    @ViewModelScoped
    fun provideAssetsUseCases(
        assetRepository: AssetRepository
    ): AssetsUseCases {
        return AssetsUseCases(
            assets = Assets(assetRepository),
            balance = AssetBalance(assetRepository),
            transactions = AssetTransactions(assetRepository)
        )
    }
}