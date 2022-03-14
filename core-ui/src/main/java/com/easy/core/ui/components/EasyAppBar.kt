package com.easy.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.easy.core.ui.LocalSpacing

@Composable
fun EasyAppBar(
    navIcon: ImageVector,
    title: String? = null,
    backgroundColor: Color,
    navigateUp: () -> Unit
) {
    TopAppBar(
        backgroundColor = backgroundColor,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .clickable { navigateUp() }
                    .padding(LocalSpacing.current.spaceSmall),
                imageVector = navIcon,
                contentDescription = ""
            )
        },
        title = {
            title?.let {
                Text(text = title)
            }
        }
    )
}