package com.easy.core.ui.common

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeGenerate {
    fun genQRCode(
        content: String,
        width: Int = 500,
        height: Int = 500
    ): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints = hashMapOf<EncodeHintType, String>().apply {
            put(EncodeHintType.CHARACTER_SET, "utf-8")
        }
        val encode = kotlin.runCatching {
            qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

        return encode?.let {
            val colors = IntArray(width * height)
            (0 until width).forEach { wi ->
                (0 until height).forEach { hi ->
                    val index = wi * width + hi
                    if (it.get(wi, hi)) {
                        colors[index] = if (index % 2 == 0) Color.BLUE else Color.GREEN
                    } else {
                        colors[index] = Color.WHITE
                    }
                }
            }
            Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565)
        }
    }
}