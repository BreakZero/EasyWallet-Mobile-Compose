package com.easy.settings.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.settings.presentation.model.MenuItem

@Composable
internal fun MenuBlockView(
    modifier: Modifier,
    header: String,
    menus: List<MenuItem>,
    onItemClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = header,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                menus.forEachIndexed { index, menuItem ->
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        menuItem = menuItem
                    ) {
                        onItemClick.invoke(index)
                    }
                    if (index < menus.size - 1) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.2.dp)
                        )
                    }
                }
            }
        }
    }
}