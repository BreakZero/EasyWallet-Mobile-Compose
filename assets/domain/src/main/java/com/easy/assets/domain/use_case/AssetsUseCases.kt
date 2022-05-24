package com.easy.assets.domain.use_case

import javax.inject.Inject

class AssetsUseCases @Inject constructor(
    val address: CoinAddress,
    val assets: Assets,
    val assetsWithBalance: AssetsWithBalance,
    val balance: AssetBalance,
    val transactions: AssetTransactions,
    val signTransaction: SignTransaction,
    val validateAddress: ValidateAddress
)