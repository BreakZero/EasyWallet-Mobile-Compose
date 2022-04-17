package com.easy.assets.presentation.send

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SendingScreen() {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = bottomSheetState,
        sheetContent = {

        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                EasyAppBar(title = "Send ETH") {

                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    Text(text = "Enter Amount")
                    BasicTextField(value = "", onValueChange = {})
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row() {
                        Text(text = "Advanced")
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                    }
                    Row() {

                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Send")
                    }
                }
            }
        }
    }
}