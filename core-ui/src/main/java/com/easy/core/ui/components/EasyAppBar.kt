package com.easy.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun EasyAppBar(
    navIcon: ImageVector = Icons.Filled.ArrowBack,
    title: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    navigateUp: () -> Unit
) {
    SmallTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navigateUp()
            }) {
                Icon(
                    imageVector = navIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        title = {
            title?.let {
                Text(
                    text = title, color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = actions
    )
}