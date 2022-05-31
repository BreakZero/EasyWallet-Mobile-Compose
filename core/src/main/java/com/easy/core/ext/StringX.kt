package com.easy.core.ext

private const val HEX_PREFIX = "0x"
fun String?.orElse(default: String): String {
    return this ?: default
}

fun String.clearHexPrefix(): String {
    return if (startsWith(HEX_PREFIX)) substring(2)
    else this
}

fun String.mark(
    size: Int,
    markChar: String = "***",
    dir: MarkDir = MarkDir.MIDDLE
): String {
    when (dir) {
        MarkDir.MIDDLE -> {
            return if (length <= size * 2) this
            else StringBuilder()
                .append(take(size))
                .append(markChar)
                .append(takeLast(size))
                .toString()
        }
        MarkDir.START -> {
            return if (length <= size) this
            else StringBuilder()
                .append(markChar)
                .append(takeLast(size))
                .toString()
        }
        MarkDir.END -> {
            return if (length <= size) this
            else StringBuilder()
                .append(take(size))
                .append(markChar)
                .toString()
        }
    }
}

enum class MarkDir {
    START, MIDDLE, END
}
