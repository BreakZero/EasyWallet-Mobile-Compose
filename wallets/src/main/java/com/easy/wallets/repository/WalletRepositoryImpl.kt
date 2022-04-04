package com.easy.wallets.repository

import com.easy.wallets.data.WalletDao
import com.easy.wallets.data.WalletEntity
import wallet.core.jni.HDWallet
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {
    lateinit var hdWallet: HDWallet
        private set
    override suspend fun insertWallet(wallet: WalletEntity) {
        walletDao.insertWallet(wallet)
    }

    override suspend fun deleteWallet(wallet: WalletEntity) {
        walletDao.deleteWallet(wallet)
    }

    override suspend fun activeWallet(): WalletEntity? {
        return walletDao.activeWallet()
    }

    override fun hdWallet(): HDWallet {
        return hdWallet
    }

    fun inject(hdWallet: HDWallet) {
        this.hdWallet = hdWallet
    }
}