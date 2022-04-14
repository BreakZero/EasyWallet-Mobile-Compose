package com.easy.assets.data

import com.easy.core.BuildConfig

internal object HttpRoutes {
    private const val EHT_RPC = "https://mainnet.infura.io/v3"
//    private const val BASE_URL = "https://ropsten.infura.io/v3"
//    private const val BASE_URL = "https://rinkeby.infura.io/v3"
    private const val CRONOS_RPC = "https://rpc.tectonic.finance"
    private const val POLYGON_RPC = "https://polygon-rpc.com"

    const val ETHEREUM_BASE_URL = "$EHT_RPC/${BuildConfig.INFURA_APIKEY}"
    const val CRONOS_BASE_URL = CRONOS_RPC
    const val POLYGON_BASE_URL = POLYGON_RPC
}