package com.easy.intro.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.easy.intro.local.entities.WalletEntity

@Database(
    entities = [WalletEntity::class],
    exportSchema = false,
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val walletDao: WalletDao
}