package com.easy.settings.presentation.model

data class MenuItem(
    val title: String,
    val subTitle: String? = null,
    val endValue: String? = null,
    val showIcon: Boolean = true
)
