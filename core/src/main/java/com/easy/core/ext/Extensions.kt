package com.easy.core.ext

import com.easy.core.common.hex
import com.easy.core.common.shex
import com.easy.core.common.unhex
import java.math.BigInteger

fun ByteArray.toHex(withPrefix: Boolean = false): String {
    return if (withPrefix) "0x${this.hex}" else this.hex
}

fun String.toHexBytes(): ByteArray {
    return this.unhex
}

fun Int.toHexByteArray(): ByteArray {
    return this.shex.unhex
}

fun BigInteger.toHexByteArray(): ByteArray {
    return this.toString(16).unhex
}

fun String._16toNumber(): BigInteger {
    return this.clearHexPrefix().toBigInteger(16)
}