package com.easy.assets.presentation.receive

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.core.ui.LocalSpacing
import com.easy.core.ui.common.QRCodeGenerate
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ReceiveScreen(
    address: String,
    navigateUp: () -> Unit
) {
    val showMenu by remember {
        mutableStateOf(false)
    }
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = true) {
        systemUIController.setStatusBarColor(Color.White, true)
    }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .clickable { navigateUp() }
                            .padding(LocalSpacing.current.spaceSmall),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                },
                title = { Text(text = "Receive") },
                actions = {
                    DropdownMenu(expanded = showMenu, onDismissRequest = { /*TODO*/ }) {

                    }
                })
        }) {
        QRCodeGenerate.genQRCode(address)?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    Text(
                        text = address,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Image(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                    Divider()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {

                            }) {
                        Text(
                            text = "Copy",
                            lineHeight = 48.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}