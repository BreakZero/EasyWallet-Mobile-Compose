package com.easy.assets.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.use_case.*
import com.easy.core.consts.ChainId
import com.easy.core.ext.byDecimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AssetDetailViewModel @AssistedInject constructor(
    assetRepository: AssetRepository,
    @Assisted private val tokenParam: AssetBundle
) : ViewModel() {
    private val assetsUseCases = AssetsUseCases(
        address = CoinAddress(assetRepository),
        balance = AssetBalance(assetRepository),
        transactions = AssetTransactions(assetRepository),
        assets = Assets(assetRepository)
    )
    @AssistedFactory
    interface Factory {
        fun create(
            tokenParam: AssetBundle
        ): AssetDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            tokenParam: AssetBundle
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(tokenParam) as T
            }
        }
    }

    var state by mutableStateOf(
        AssetDetailState(
            isLoading = true,
            result = Result.success(emptyList()),
            balance = Result.success("--"),
            icon = "https://s3-ap-southeast-1.amazonaws.com/monaco-cointrack-production/uploads/coin/colorful_logo/5f647d8d97713d009777e1cd/UNI_4x.png",
            symbol = tokenParam.symbol
        )
    )
        private set

    init {
        viewModelScope.launch {
            val balance = async {
                assetsUseCases.balance(
                    assetsUseCases.address(tokenParam.slug),
                    ChainId.ETHEREUM,
                    tokenParam.contractAddress
                )
            }

            val txList = async {
                assetsUseCases.transactions(
                    assetsUseCases.address(tokenParam.slug),
                    ChainId.ETHEREUM,
                    offset = 20,
                    limit = 10,
                    contractAddress = tokenParam.contractAddress
                )
            }
            state = state.copy(
                isLoading = false, result = txList.await(),
                balance = Result.success(balance.await().byDecimal(8, 8))
            )
        }
    }

    fun address(): String {
        return assetsUseCases.address(tokenParam.slug, false)
    }
}