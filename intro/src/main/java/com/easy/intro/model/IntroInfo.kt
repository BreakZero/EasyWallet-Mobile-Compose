package com.easy.intro.model

import androidx.annotation.DrawableRes
import androidx.annotation.Keep

@Keep
data class IntroInfo(
    val title: String,
    val tips: String,
    @DrawableRes val iconRes: Int
)