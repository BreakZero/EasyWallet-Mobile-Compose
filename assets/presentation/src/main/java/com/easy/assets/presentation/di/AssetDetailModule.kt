package com.easy.assets.presentation.di

import com.easy.assets.presentation.detail.AssetDetailViewModel
import com.easy.assets.presentation.send.SendingViewModel
import com.easy.assets.presentation.send.address.SendAddressViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun assetDetailViewModelFactory(): AssetDetailViewModel.Factory
    fun assetSendAddressViewModelFactory(): SendAddressViewModel.Factory
    fun assetSendViewModelFactory(): SendingViewModel.Factory
}
