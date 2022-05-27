package com.easy.assets.data.model.local

import androidx.room.*

@Dao
interface ERC20TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(entity: ERC20TokenEntity)

    @Delete
    suspend fun deleteToken(entity: ERC20TokenEntity)

    @Query(
        """
        SELECT * 
        FROM TB_ERC20_TOKEN
        """
    )
    fun tokens(): List<ERC20TokenEntity>?
}