package com.easy.intro.local

import androidx.room.*
import com.easy.intro.local.entities.WalletEntity
import kotlinx.coroutines.flow.Flow

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
    fun activeWallet(): Flow<WalletEntity?>
}