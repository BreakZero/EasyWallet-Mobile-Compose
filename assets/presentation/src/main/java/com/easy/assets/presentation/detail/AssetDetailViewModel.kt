package com.easy.assets.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.core.ext.byDecimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AssetDetailViewModel @AssistedInject constructor(
    private val assetsUseCases: AssetsUseCases,
    @Assisted private val slug: String
) : ViewModel() {
    private var currAsset: AssetInfo? = null

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
            transactions = Result.success(emptyList()),
            balance = Result.success("--")
        )
    )
        private set

    init {
        viewModelScope.launch {
            currAsset = assetsUseCases.assets().find { it.slug == slug }
            currAsset?.let {
                state = state.copy(assetInfo = it)
                val balance = async {
                    assetsUseCases.balance(
                        it.slug,
                        it.contractAddress
                    )
                }

                val txList = async {
                    assetsUseCases.transactions(
                        slug = it.slug,
                        offset = 20,
                        limit = 10,
                        contract = it.contractAddress
                    )
                }
                state = state.copy(
                    assetInfo = it,
                    isLoading = false, transactions = txList.await(),
                    balance = Result.success(balance.await().byDecimal(it.decimal, 8))
                )
            }
        }
    }

    fun onEvent(event: AssetDetailEvent) {
        when (event) {
            is AssetDetailEvent.OnRefresh -> {
                state = state.copy(isLoading = true)
                viewModelScope.launch(Dispatchers.IO) {
                    currAsset?.let {
                        val txList = assetsUseCases.transactions(
                            slug = it.slug,
                            offset = 20,
                            limit = 10,
                            contract = it.contractAddress
                        )
                        state = state.copy(transactions = txList, isLoading = false)
                    }
                }
            }
            else -> Unit
        }
    }

    fun address(): String {
        return assetsUseCases.address(slug)
    }
}