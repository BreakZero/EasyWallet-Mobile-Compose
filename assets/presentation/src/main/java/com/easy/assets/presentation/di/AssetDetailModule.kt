package com.easy.assets.presentation.di

import com.easy.assets.presentation.detail.AssetDetailViewModel
import com.easy.assets.presentation.send.SendingViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun assetDetailViewModelFactory(): AssetDetailViewModel.Factory
    fun assetSendViewModelFactory(): SendingViewModel.Factory
}
