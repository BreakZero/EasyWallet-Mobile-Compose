package com.easy.assets.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.use_case.AssetBalance
import com.easy.assets.domain.use_case.AssetTransactions
import com.easy.assets.domain.use_case.Assets
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.core.consts.ChainId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import logcat.logcat

class AssetDetailViewModel @AssistedInject constructor(
    assetRepository: AssetRepository,
    @Assisted private val tokenParam: AssetBundle
) : ViewModel() {
    private val assetsUseCases = AssetsUseCases(
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
            /*val balance = ethereumRepositoryImpl.balance(
                "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5",
                ChainId.ETHEREUM,
                tokenParam.contractAddress
            )*/
            logcat {
                "===== $tokenParam"
            }
            val txList = assetsUseCases.transactions(
                "0x81080a7e991bcdddba8c2302a70f45d6bd369ab5",
                ChainId.ETHEREUM,
                offset = 0,
                limit = 10,
                contractAddress = tokenParam.contractAddress
            )
            state = state.copy(isLoading = false, result = txList, balance = Result.success("0.2333"))
        }
    }
}