package com.easy.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.easy.core.ui.LocalSpacing

@Composable
fun EasyActionBar(
    @DrawableRes navIcon: Int,
    @DrawableRes menuIcons: List<Int>,
    backgroundColor: Color,
    tint: Color,
    onNavClick: () -> Unit,
    onMenuClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onNavClick.invoke()
                }
            ) {
                Image(
                    modifier = Modifier
                        .size(LocalSpacing.current.space48)
                        .padding(LocalSpacing.current.spaceSmall),
                    painter = painterResource(id = navIcon),
                    contentDescription = ""
                )
            }
            Column {
                Text(
                    text = "Wallet Name",
                    color = tint
                )
                Text(
                    text = "View Settings",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        menuIcons.forEachIndexed { index, icon ->
            IconButton(onClick = {
                onMenuClick.invoke(index)
            }) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(LocalSpacing.current.space48)
                        .padding(LocalSpacing.current.space12),
                    tint = tint
                )
            }
        }
    }
}