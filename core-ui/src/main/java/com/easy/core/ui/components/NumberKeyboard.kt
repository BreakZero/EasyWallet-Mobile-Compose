package com.easy.core.ui.components

import androidx.annotation.Keep
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Keep
data class DataItem(
    val actionType: ActionType,
    val number: String = ""
)

enum class ActionType {
    NUMBER, SPACE, BACKSPACE
}

private val KEYBOARD_NUMBERS = listOf(
    DataItem(actionType = ActionType.NUMBER, number = "1"),
    DataItem(actionType = ActionType.NUMBER, number = "2"),
    DataItem(actionType = ActionType.NUMBER, number = "3"),
    DataItem(actionType = ActionType.NUMBER, number = "4"),
    DataItem(actionType = ActionType.NUMBER, number = "5"),
    DataItem(actionType = ActionType.NUMBER, number = "6"),
    DataItem(actionType = ActionType.NUMBER, number = "7"),
    DataItem(actionType = ActionType.NUMBER, number = "8"),
    DataItem(actionType = ActionType.NUMBER, number = "9"),
    DataItem(actionType = ActionType.SPACE),
    DataItem(actionType = ActionType.NUMBER, number = "0"),
    DataItem(actionType = ActionType.BACKSPACE)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NumberKeyboard(
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    onNumberClick: (DataItem) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        columns = GridCells.Fixed(3)
    ) {
        itemsIndexed(KEYBOARD_NUMBERS) { index, item ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth()
                            .clickable {
                                onNumberClick.invoke(item)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (item.actionType) {
                            ActionType.NUMBER -> {
                                Text(text = item.number, fontWeight = FontWeight.Bold)
                            }
                            ActionType.BACKSPACE -> {
                                Icon(
                                    imageVector = Icons.Outlined.Backspace,
                                    contentDescription = null
                                )
                            }
                            ActionType.SPACE -> {
                                Spacer(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                    if (showDivider) {
                        Divider() // Horizontal divider
                    }
                }

                // Vertical divider avoiding the last cell in each row
                if (showDivider && (index + 1) % 3 != 0) {
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
