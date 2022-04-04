package com.easy.assets.presentation.send.amount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.assets.presentation.send.SendInfo
import com.easy.assets.presentation.send.SendInfoHolder
import com.easy.core.common.Navigator
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun NormalSendAmountScreen(
    sendViewModel: SendAmountViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            EasyAppBar(
                title = "Send",
                navIcon = Icons.Filled.ArrowBack,
                backgroundColor = Color.White
            ) {
                navigateUp()
            }
        }) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .padding(2.dp)
                            .padding(top = 4.dp, bottom = 4.dp),
                        value = sendViewModel.state.amount,
                        onValueChange = {
                            sendViewModel.onAmountChanged(it)
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                    SwitchOption(
                        modifier = Modifier.fillMaxHeight(),
                        options = "ETH" to "USD",
                        onSwitchChanged = {

                        })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$ 258.75",
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                    Row() {
                        Text(text = "Available ", modifier = Modifier.alignByBaseline())
                        Text(text = "0.002 ETH", modifier = Modifier
                            .alignByBaseline()
                            .drawBehind {
                                val strokeWidth = 1.dp.value * density
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    Color.Green,
                                    Offset(0f, y),
                                    Offset(size.width, y),
                                    strokeWidth
                                )
                            })
                    }
                }
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .padding(2.dp)
                        .padding(top = 4.dp, bottom = 4.dp),
                    value = sendViewModel.state.memo,
                    onValueChange = {
                        sendViewModel.onMemoChanged(it)
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )
                Divider()
                Button(modifier = Modifier
                    .fillMaxWidth(),
                    onClick = {
                        SendInfoHolder.submitField(
                            SendInfo.Field.AMOUNT,
                            sendViewModel.state.amount
                        )
                        SendInfoHolder.submitField(SendInfo.Field.MEMO, sendViewModel.state.memo)
                    }) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
private fun SwitchOption(
    modifier: Modifier,
    options: Pair<String, String>,
    checked: Boolean = true,
    onSwitchChanged: (Boolean) -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .alignByBaseline()
            .clickable {
                onSwitchChanged(true)
            }
            .padding(4.dp)
        ) {
            Text(text = options.first, color = if (checked) Color.Black else Color.Gray)
        }
        Box(
            modifier = Modifier
                .width(0.2.dp)
                .height(24.dp)
                .alignByBaseline()
                .background(Color.Gray)
        )
        Box(modifier = Modifier
            .alignByBaseline()
            .clickable {
                onSwitchChanged(false)
            }
            .padding(4.dp)) {
            Text(text = options.second, color = if (checked) Color.Gray else Color.Black)
        }
    }
}