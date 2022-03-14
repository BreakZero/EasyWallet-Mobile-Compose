package com.easy.intro.walletimport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.common.UiEvent
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.spacing
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WalletImportScreen(
    scaffoldState: ScaffoldState,
    viewModel: WalletImportViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onNavigateToAssets: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val state = viewModel.state
    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    onNavigateToAssets.invoke()
                }
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> {
                    onNavigateUp.invoke()
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                title = "Import Wallet",
                navIcon = Icons.Filled.ArrowBack,
                backgroundColor = Color.White
            ) {
                viewModel.onNavigateUp()
                keyboardController?.hide()
            }
        }
    ) {
        Column() {
            TextField(
                value = state.phrase,
                onValueChange = {
                    viewModel.onEvent(ImportEvent.OnPhraseChange(it))
                },
                textStyle = TextStyle(color = Color.Black),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier
                    .padding(MaterialTheme.spacing.spaceMedium)
                    .height(200.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        viewModel.onEvent(ImportEvent.OnFocusChange(it.isFocused))
                    }
            )
            Button(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(24.dp),
                onClick = {
                    viewModel.onEvent(ImportEvent.OnImportClick)
                    keyboardController?.hide()
                }) {
                Text(text = "Import")
            }
        }
    }
}