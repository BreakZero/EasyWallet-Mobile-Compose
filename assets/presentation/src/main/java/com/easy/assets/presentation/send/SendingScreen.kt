package com.easy.assets.presentation.send

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.core.common.Navigator
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.components.NumberKeyboard
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
            EnterAmountKeyboard()
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
                    Image(painter = rememberImagePainter(
                        data = sendViewModel.sendingState.assetInfo?.icon,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ), modifier = Modifier.size(64.dp), contentDescription = null)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EnterAmountKeyboard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Remaining Balance")
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        Text(text = "xxx ETH")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)
        ) {
            NumberKeyboard(
                modifier = Modifier
                    .fillMaxWidth(0.75F),
                showDivider = false
            ) {

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                        .fillMaxHeight(0.75F)
                        .fillMaxWidth()
                ) {
                    Text(text = "Enter")
                }
                Button(
                    onClick = { }, modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Text(text = "All")
                }
            }
        }
    }
}