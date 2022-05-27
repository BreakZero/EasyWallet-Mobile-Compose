package com.easy.assets.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CoinConfigDto(
coinSlug = "ethereum",
coinSymbol = "ETH",
contractAddress = null,
decimal = 18,
iconUrl = "https://easywallet.s3.amazonaws.com/wallet-icons/ethereum.png",
tag = null
)
 */
@Entity(tableName = "tb_erc20_token")
data class ERC20TokenEntity(
    @PrimaryKey
    @ColumnInfo(name = "coin_slug")
    val coinSlug: String,
    @ColumnInfo(name = "coin_symbol")
    val coinSymbol: String,
    @ColumnInfo(name = "contract_address")
    val contractAddress: String,
    @ColumnInfo(name = "decimal")
    val decimal: Int,
    @ColumnInfo(name = "icon_url")
    val iconUrl: String,
    @ColumnInfo(name = "coin_tag")
    val tag: String = "ERC20"
)
