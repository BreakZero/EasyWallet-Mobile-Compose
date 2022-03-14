package com.easy.assets.domain.use_case

data class AssetsUseCases(
    val assets: Assets,
    val balance: AssetBalance,
    val transactions: AssetTransactions
)