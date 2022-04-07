package com.easy.wallets.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WalletEntity::class],
    exportSchema = false,
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val walletDao: WalletDao
}