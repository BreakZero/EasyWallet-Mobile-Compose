package com.easy.assets.data.mapper

import com.easy.assets.data.remote.dto.EthTxDto
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