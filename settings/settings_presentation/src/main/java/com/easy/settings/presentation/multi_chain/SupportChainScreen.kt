package com.easy.settings.presentation.multi_chain

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.easy.core.ui.components.EasyAppBar

@Composable
fun SupportChainScreen() {
    Scaffold(
        topBar = {
            EasyAppBar(navIcon = Icons.Filled.ArrowBack, backgroundColor = Color.White) {
                
            }
        }
    ) {
        LazyColumn {

        }
    }
}