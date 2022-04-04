package com.easy.wallets.repository

import com.easy.wallets.data.WalletEntity

interface WalletRepository {
    suspend fun insertWallet(wallet: WalletEntity)
    suspend fun deleteWallet(wallet: WalletEntity)
    suspend fun activeWallet(): WalletEntity?
}