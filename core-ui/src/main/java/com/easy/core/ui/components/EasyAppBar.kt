package com.easy.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun EasyAppBar(
    navIcon: ImageVector = Icons.Filled.ArrowBack,
    title: String? = null,
    backgroundColor: Color = Color.White,
    actions: @Composable RowScope.() -> Unit = {},
    navigateUp: () -> Unit
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        navigationIcon = {
            IconButton(onClick = {
                navigateUp()
            }) {
                Icon(
                    imageVector = navIcon,
                    contentDescription = ""
                )
            }
        },
        title = {
            title?.let {
                Text(text = title)
            }
        },
        actions = actions
    )
}