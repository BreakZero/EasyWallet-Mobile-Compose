package com.easy.dapp.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.easy.core.BuildConfig
import com.easy.dapp.domain.model.DAppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DAppViewModel @Inject constructor(): ViewModel() {
    var dAppState by mutableStateOf(DAppState(dApps = Result.success(emptyList()), isLoading = true))
    private set

    init {
        dAppState = dAppState.copy(
            dApps = Result.success(
                listOf(
                    DAppInfo(
                        icon = "https://cryptologos.cc/logos/uniswap-uni-logo.png",
                        name = "Sushi Swap",
                        url = "https://app.sushi.com/en/swap",
                        chain = 1,
                        rpc = "https://mainnet.infura.io/v3/${BuildConfig.INFURA_APIKEY}"
                    ),
                    DAppInfo(
                        icon = "https://cryptologos.cc/logos/uniswap-uni-logo.png",
                        name = "Uni Swap",
                        url = "https://app.uniswap.org/swap",
                        chain = 1,
                        rpc = "https://mainnet.infura.io/v3/${BuildConfig.INFURA_APIKEY}"
                    ),
                    DAppInfo(
                        icon = "https://img2.baidu.com/it/u=3117495242,168525747&fm=26&fmt=auto&gp=0.jpg",
                        name = "Pancake swap",
                        url = "https://pancakeswap.finance/swap",
                        chain = 56,
                        rpc = "https://bsc-dataseed.binance.org"
                    ),
                    DAppInfo(
                        icon = "https://img2.baidu.com/it/u=3117495242,168525747&fm=26&fmt=auto&gp=0.jpg",
                        name = "Simple Link",
                        url = "https://js-eth-sign.surge.sh",
                        chain = 56,
                        rpc = "https://bsc-dataseed2.binance.org"
                    )
                )
            ), isLoading = false
        )
    }
}