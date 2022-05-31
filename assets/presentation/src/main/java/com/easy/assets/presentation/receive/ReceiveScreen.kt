package com.easy.assets.presentation.receive

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.core.ui.common.QRCodeGenerate
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveScreen(
    address: String,
    navigateUp: () -> Unit
) {
    val showMenu by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            EasyAppBar(
                title = "Receive",
                actions = {
                    DropdownMenu(expanded = showMenu, onDismissRequest = { /*TODO*/ }) {
                    }
                }
            ) {
                navigateUp()
            }
        }
    ) { paddingValue ->
        QRCodeGenerate.genQRCode(address)?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValue)
                    .padding(MaterialTheme.spacing.spaceLarge),
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    Text(
                        text = address,
                        modifier = Modifier.padding(MaterialTheme.spacing.space12),
                        textAlign = TextAlign.Center
                    )
                    Image(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        alignment = Alignment.Center
                    )
                    Divider(modifier = Modifier.padding(top = MaterialTheme.spacing.space12))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                            }
                    ) {
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
