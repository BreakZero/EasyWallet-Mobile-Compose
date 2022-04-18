package com.easy.assets.presentation.send

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.core.common.Navigator
import com.easy.core.ui.components.EasyAppBar
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@Composable
fun assetSendViewModel(
    slug: String
): SendingViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).assetSendViewModelFactory()

    return viewModel(
        factory = SendingViewModel.provideFactory(
            factory,
            slug
        )
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SendingScreen(
    slug: String,
    sendViewModel: SendingViewModel = assetSendViewModel(slug),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = bottomSheetState,
        sheetContent = {
            Column() {
                Text(text = "Item 1")
                Text(text = "Item 2")
                Text(text = "Item 3")
                Text(text = "Item 4")
                Text(text = "Item 3")
            }
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    Text(text = "Enter Amount")
                    BasicTextField(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        value = "",
                        onValueChange = {})
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
                    OutlinedButton(onClick = {
                        scope.launch {
                            bottomSheetState.show()
                        }
                    }) {
                        Text(text = "Send")
                    }
                }
            }
        }
    }
}