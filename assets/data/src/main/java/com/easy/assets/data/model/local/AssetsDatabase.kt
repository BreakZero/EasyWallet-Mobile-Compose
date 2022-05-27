package com.easy.assets.data.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ERC20TokenEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AssetsDatabase : RoomDatabase() {
    abstract val tokenDao: ERC20TokenDao
}