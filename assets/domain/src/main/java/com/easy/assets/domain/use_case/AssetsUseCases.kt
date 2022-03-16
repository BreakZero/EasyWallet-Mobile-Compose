package com.easy.assets.domain.use_case

data class AssetsUseCases(
    val address: CoinAddress,
    val assets: Assets,
    val assetsWithBalance: AssetsWithBalance,
    val balance: AssetBalance,
    val transactions: AssetTransactions
)