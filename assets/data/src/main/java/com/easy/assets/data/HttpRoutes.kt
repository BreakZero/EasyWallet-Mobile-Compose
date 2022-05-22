package com.easy.assets.data

import com.easy.core.BuildConfig

internal object HttpRoutes {
    const val MAINNET_RPC = "https://mainnet.infura.io/v3/${BuildConfig.INFURA_APIKEY}"
    const val MAINNET_EXPLORER = "https://api.etherscan.io/api"
    const val ROPSTEN_RPC = "https://ropsten.infura.io/v3/${BuildConfig.INFURA_APIKEY}"
    const val ROPSTEN_EXPLORER = "https://api-ropsten.etherscan.io/api"
    const val RINKEBY_RPC = "https://rinkeby.infura.io/v3/${BuildConfig.INFURA_APIKEY}"
    const val RINKEBY_EXPLORER = "https://api-rinkeby.etherscan.io/api"


    const val CRONOS_MAINNET_RPC = "https://rpc.tectonic.finance"
    const val CRONOS_MAINNET_EXPLORER = "https://cronos.org/explorer/api"
    const val CRONOS_TESTNET_RPC = "https://rpc.tectonic.finance"
    const val CRONOS_TESTNET_EXPLORER = "https://cronos.org/explorer/api"

    const val POLYGON_MAINNET_RPC = "https://polygon-rpc.com"
    const val POLYGON_MAINNET_EXPLORER = "https://api.polygonscan.com/api"
    const val POLYGON_TESTNET_RPC = "https://polygon-rpc.com"
    const val POLYGON_TESTNET_EXPLORER = "https://api.polygonscan.com/api"

    const val SOLANA_MAINNET_RPC = "https://api.testnet.solana.com"
    const val SOLANA_MAINNET_EXPLORER = "https://public-api.solscan.io"
    const val SOLANA_TESTNET_RPC = "https://api.testnet.solana.com"
    const val SOLANA_TESTNET_EXPLORER = "https://public-api.solscan.io"
}