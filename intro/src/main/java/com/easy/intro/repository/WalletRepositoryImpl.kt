package com.easy.intro.repository

import com.easy.intro.local.WalletDao
import com.easy.intro.local.entities.WalletEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    override suspend fun insertWallet(wallet: WalletEntity) {
        walletDao.insertWallet(wallet)
    }

    override suspend fun deleteWallet(wallet: WalletEntity) {
        walletDao.deleteWallet(wallet)
    }

    override suspend fun activeWallet(): Flow<WalletEntity?> {
        return walletDao.activeWallet()
    }
}