package com.easy.assets.data.mapper

import com.easy.assets.data.remote.dto.EthTxDto
import com.easy.assets.domain.model.Transaction


internal fun EthTxDto.toTransaction(): Transaction {
    return Transaction(
        txHash = this.hash,
        value = this.value,
        from = this.from,
        to = this.to,
        timeStamp = this.timeStamp,
        inputData = this.input
    )
}