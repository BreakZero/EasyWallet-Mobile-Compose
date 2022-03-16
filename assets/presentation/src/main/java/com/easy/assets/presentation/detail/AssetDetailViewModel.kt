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
    @Assisted private val slug: String
) : ViewModel() {
    private val assetsUseCases = AssetsUseCases(
        address = CoinAddress(assetRepository),
        balance = AssetBalance(assetRepository),
        assetsWithBalance = AssetsWithBalance(assetRepository),
        transactions = AssetTransactions(assetRepository),
        assets = Assets(assetRepository)
    )
    @AssistedFactory
    interface Factory {
        fun create(slug: String): AssetDetailViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            slug: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(slug) as T
            }
        }
    }

    var state by mutableStateOf(
        AssetDetailState(
            isLoading = true,
            result = Result.success(emptyList()),
            balance = Result.success("--")
        )
    )
        private set

    init {
        viewModelScope.launch {
            val currAsset = assetsUseCases.assets().find { it.slug == slug }
            currAsset?.let {
                state = state.copy(assetInfo = it)
                val balance = async {
                    assetsUseCases.balance(
                        assetsUseCases.address(it.slug),
                        ChainId.ETHEREUM,
                        it.contractAddress
                    )
                }

                val txList = async {
                    assetsUseCases.transactions(
                        assetsUseCases.address(it.slug),
                        ChainId.ETHEREUM,
                        offset = 20,
                        limit = 10,
                        contractAddress = it.contractAddress
                    )
                }
                state = state.copy(
                    assetInfo = it,
                    isLoading = false, result = txList.await(),
                    balance = Result.success(balance.await().byDecimal(8, 8))
                )
            }
        }
    }

    fun address(): String {
        return assetsUseCases.address(slug, false)
    }
}