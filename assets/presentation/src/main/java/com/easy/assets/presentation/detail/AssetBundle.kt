package com.easy.assets.presentation.detail

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AssetBundle(
    val slug: String,
    val symbol: String,
    val contractAddress: String
): Parcelable
