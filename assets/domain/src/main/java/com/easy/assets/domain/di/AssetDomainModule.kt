package com.easy.assets.domain.di

import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
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
        assetRepository: AssetRepository,
        coinRepository: CoinRepository
    ): AssetsUseCases {
        return AssetsUseCases(
            address = CoinAddress(coinRepository),
            assets = Assets(coinRepository),
            balance = AssetBalance(assetRepository),
            assetsWithBalance = AssetsWithBalance(coinRepository, assetRepository),
            transactions = AssetTransactions(assetRepository),
            signTransaction = SignTransaction(assetRepository),
            validateAddress = ValidateAddress(coinRepository)
        )
    }
}