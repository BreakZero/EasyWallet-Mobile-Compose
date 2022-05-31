package com.easy.wallets.data

import androidx.room.*

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(entity: WalletEntity)

    @Delete
    suspend fun deleteWallet(entity: WalletEntity)

    @Query(
        """
        SELECT * 
        FROM TB_WALLET 
        WHERE active = 1
        """
    )
    fun activeWallet(): WalletEntity?
}
