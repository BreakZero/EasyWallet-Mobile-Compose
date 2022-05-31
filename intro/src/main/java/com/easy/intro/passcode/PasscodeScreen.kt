package com.easy.intro.passcode

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.ActionType
import com.easy.core.ui.components.NumberKeyboard
import com.easy.intro.R

@Composable
fun PasscodeScreen(
    viewModel: PasscodeViewModel = hiltViewModel(),
    onNavigateUp: (String) -> Unit
) {
    LaunchedEffect(key1 = null) {
        viewModel.uiEvent.collect {
            val passcode = it.getOrNull().orEmpty()
            if (it.isSuccess && passcode.isNotEmpty()) {
                viewModel.onEvent(PasscodeEvent.Done)
                onNavigateUp(passcode)
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF192B5E)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.banner_welcome),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = viewModel.passcodeState.messageLabel,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val enterSize = viewModel.passcodeState.passcode.length
                    if (enterSize == 0) {
                        (0 until 6).forEach { _ ->
                            Canvas(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .height(16.dp)
                                    .width(16.dp)
                            ) {
                                drawCircle(color = Color.White, style = Stroke(width = 1.5f))
                            }
                        }
                    } else {
                        (0 until enterSize).forEach { _ ->
                            Canvas(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .height(16.dp)
                                    .width(16.dp)

                            ) {
                                drawCircle(color = Color.White)
                            }
                        }
                        (enterSize until 6).forEach { _ ->
                            Canvas(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .height(16.dp)
                                    .width(16.dp)
                            ) {
                                drawCircle(color = Color.White, style = Stroke(width = 1.5f))
                            }
                        }
                    }
                }
                viewModel.passcodeState.error?.let {
                    Text(
                        text = it, color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            NumberKeyboard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                onNumberClick = {
                    when (it.actionType) {
                        ActionType.NUMBER -> viewModel.onEvent(PasscodeEvent.Insert(it.number))
                        ActionType.BACKSPACE -> viewModel.onEvent(PasscodeEvent.Delete)
                        else -> Unit
                    }
                }
            )
        }
    }
}
