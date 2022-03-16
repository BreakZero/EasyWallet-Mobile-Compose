package com.easy.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.settings.model.MenuItem

@Composable
internal fun MenuItemView(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    action: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                action.invoke()
            }
            .padding(start = 12.dp, end = 12.dp)
            .defaultMinSize(minHeight = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(text = menuItem.title)
            menuItem.subTitle?.let {
                Text(text = it, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Row() {
            menuItem.endValue?.let {
                Text(text = it, fontSize = 14.sp, color = Color.Gray)
            }
            if (menuItem.showIcon) {
                Icon(
                    imageVector = Icons.Filled.ArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}