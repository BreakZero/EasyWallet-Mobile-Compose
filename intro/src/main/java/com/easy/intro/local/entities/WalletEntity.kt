package com.easy.intro.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_wallet"
)
data class WalletEntity(
    @PrimaryKey val mnemonic: String,
    val active: Int, // 0 = inactive, 1 = active
    val passphrase: String
)