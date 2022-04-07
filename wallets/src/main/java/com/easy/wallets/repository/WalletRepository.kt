package com.easy.wallets.repository

import com.easy.wallets.data.WalletEntity
import wallet.core.jni.HDWallet

interface WalletRepository {
    suspend fun insertWallet(wallet: WalletEntity)
    suspend fun deleteWallet(wallet: WalletEntity)
    suspend fun activeWallet(): WalletEntity?

    fun hdWallet(): HDWallet
}