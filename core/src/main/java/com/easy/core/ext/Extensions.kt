package com.easy.core.ext

import com.google.protobuf.ByteString

fun ByteArray.toHex(): String {
    val output = String(toHexCharArray(this, 0, this.size, true))
    return StringBuilder(HEX_PREFIX).append(output).toString()
}

fun String.toHexBytes(): ByteArray {
    return hexStringToByteArray(this)
}

fun String.toByteString(): ByteString {
    return ByteString.copyFrom(this, Charsets.UTF_8)
}

fun String.toHexBytesInByteString(): ByteString {
    return ByteString.copyFrom(this.toHexBytes())
}

private const val HEX_PREFIX = "0x"
private val HEX_CHAR_MAP = "0123456789abcdef".toCharArray()
private fun toHexCharArray(
    input: ByteArray,
    offset: Int,
    length: Int,
    withPrefix: Boolean
): CharArray {
    val output = CharArray(length shl 1)
    var i = offset
    var j = 0
    while (i < length) {
        val v: Int = input[i].toInt() and 0xFF
        output[j++] = HEX_CHAR_MAP[v ushr 4]
        output[j] = HEX_CHAR_MAP[v and 0x0F]
        i++
        j++
    }
    return output
}

private fun hexStringToByteArray(input: String): ByteArray {
    val cleanInput: String = input.clearHexPrefix()
    val len = cleanInput.length
    if (len == 0) {
        return byteArrayOf()
    }
    val data: ByteArray
    val startIdx: Int
    if (len % 2 != 0) {
        data = ByteArray(len / 2 + 1)
        data[0] = (cleanInput[0].digitToIntOrNull(16) ?: -1).toByte()
        startIdx = 1
    } else {
        data = ByteArray(len / 2)
        startIdx = 0
    }
    var i = startIdx
    while (i < len) {
        data[(i + 1) / 2] = ((((cleanInput[i].digitToIntOrNull(16)
            ?: (-1 shl 4)) + cleanInput[i + 1].digitToIntOrNull(16)!!) ?: -1)).toByte()
        i += 2
    }
    return data
}
