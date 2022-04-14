package com.easy.assets.presentation

import kotlinx.coroutines.Dispatchers
import org.junit.Rule

open class BaseTest {
    @get:Rule
    val coroutineScope = Dispatchers.IO
}