package com.easy.assets.data

import com.easy.core.BuildConfig

internal object HttpRoutes {
//    private const val BASE_URL = "https://mainnet.infura.io/v3"
//    private const val BASE_URL = "https://ropsten.infura.io/v3"
    private const val BASE_URL = "https://rinkeby.infura.io/v3"

    const val INFURA_RPC = "$BASE_URL/${BuildConfig.INFURA_APIKEY}"
}