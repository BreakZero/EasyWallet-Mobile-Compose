package com.easy.assets.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.assets.presentation.detail.paging.TransactionPagingSource
import com.easy.core.ext.byDecimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(slug) as T
            }
        }
    }

    var state by mutableStateOf(
        AssetDetailState(
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
                        it.chain,
                        it.contractAddress
                    )
                }

                state = state.copy(
                    assetInfo = it,
                    balance = Result.success(balance.await().byDecimal(it.decimal, 8)),
                    pager = Pager(
                        PagingConfig(pageSize = 20)
                    ) {
                        TransactionPagingSource(assetsUseCases.transactions, it)
                    }
                )
            }
        }
    }

    fun onEvent(event: AssetDetailEvent) {
        when (event) {
            is AssetDetailEvent.OnRefresh -> {
                currAsset?.let {
                    state = state.copy(
                        pager = Pager(
                            PagingConfig(pageSize = 20)
                        ) {
                            TransactionPagingSource(assetsUseCases.transactions, it)
                        }
                    )
                }
            }
        }
    }

    fun address(): String {
        return currAsset?.let {
            assetsUseCases.address(it.chain)
        }.orEmpty()
    }
}
