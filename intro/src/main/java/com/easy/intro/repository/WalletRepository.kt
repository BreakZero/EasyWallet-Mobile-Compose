package com.easy.intro.repository

import com.easy.intro.local.entities.WalletEntity
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    suspend fun insertWallet(wallet: WalletEntity)
    suspend fun deleteWallet(wallet: WalletEntity)
    suspend fun activeWallet(): Flow<WalletEntity?>
}