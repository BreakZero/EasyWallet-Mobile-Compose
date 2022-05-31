package com.easy.core.ext

import java.math.BigInteger
import java.math.RoundingMode

fun BigInteger.byDecimal(
    decimal: Int,
    display: Int
): String {
    return this.toBigDecimal()
        .movePointLeft(decimal)
        .setScale(display, RoundingMode.DOWN)
        .toPlainString()
}
