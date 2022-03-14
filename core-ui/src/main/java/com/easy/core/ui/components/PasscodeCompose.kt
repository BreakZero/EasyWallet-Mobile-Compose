package com.easy.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Passcode(
    modifier: Modifier = Modifier,
    random: Boolean,
    onNumberClick: (String) -> Unit
) {
    val numbers = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "<"
    )
    LazyVerticalGrid(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        cells = GridCells.Fixed(3)
    ) {
        itemsIndexed(numbers) { index, item ->
            Row(Modifier.height(IntrinsicSize.Min)) {

                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .clickable {
                                onNumberClick.invoke(item.toString())
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.toString())
                    }
                    Divider() //Horizontal divider
                }

                //Vertical divider avoiding the last cell in each row
                if ((index + 1) % 3 != 0) {
                    Column() {
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                }
            }
        }
    }
}