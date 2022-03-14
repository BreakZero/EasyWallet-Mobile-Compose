package com.easy.assets.presentation.send

import androidx.annotation.Keep

object SendInfoHolder {
    private var _info: SendInfo? = null

    fun submitField(field: SendInfo.Field, value: Any) {
        _info = (_info ?: SendInfo()).let {
            when (field) {
                SendInfo.Field.ADDRESS -> it.copy(address = value as String)
                SendInfo.Field.AMOUNT -> it.copy(amount = value as String)
                SendInfo.Field.GAS -> it.copy(gas = value as Long)
                SendInfo.Field.GAS_LIMIT -> it.copy(gasLimit = value as Long)
                SendInfo.Field.MEMO -> it.copy(memo = value as String)
            }
        }
    }

    fun info() = _info

    fun clear() {
        _info = null
    }
}

@Keep
data class SendInfo(
    val address: String = "",
    val amount: String = "",
    val memo: String? = null,
    val gas: Long = 0L,
    val gasLimit: Long = 0L
) {
    enum class Field {
        ADDRESS,
        AMOUNT,
        GAS,
        GAS_LIMIT,
        MEMO
    }
}
