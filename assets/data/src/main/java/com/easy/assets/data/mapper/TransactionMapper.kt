package com.easy.assets.data.mapper

import com.easy.assets.data.model.remote.dto.EthTxDto
import com.easy.assets.data.model.remote.dto.SolTransactionDto
import com.easy.assets.domain.model.Transaction

internal fun EthTxDto.toTransaction(address: String): Transaction {
    return Transaction(
        txHash = this.hash,
        value = this.value,
        from = this.from,
        to = this.to,
        isReceive = this.to.equals(address, true),
        timeStamp = this.timeStamp,
        inputData = this.input
    )
}

internal fun SolTransactionDto.toTransaction(
    address: String
): Transaction {
    return Transaction(
        txHash = this.txHash,
        value = "0",
        from = address,
        to = "",
        isReceive = false,
        timeStamp = this.blockTime.toString(),
        inputData = ""
    )
}