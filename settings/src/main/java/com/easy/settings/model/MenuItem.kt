package com.easy.settings.model

data class MenuItem(
    val title: String,
    val subTitle: String? = null,
    val endValue: String? = null,
    val showIcon: Boolean = true
)
