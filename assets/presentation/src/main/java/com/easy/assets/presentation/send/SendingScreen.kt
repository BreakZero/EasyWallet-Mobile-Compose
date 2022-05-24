package com.easy.assets.presentation.send

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.core.common.Navigator
import com.easy.core.common.UiEvent
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.spacing
import com.google.accompanist.insets.navigationBarsWithImePadding
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

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SendingScreen(
    slug: String,
    sendViewModel: SendingViewModel = assetSendViewModel(slug),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val uiState = sendViewModel.sendingState
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {
        sendViewModel.uiEvent.collect {
            when(it) {
                UiEvent.Success -> {
                    bottomSheetState.show()
                }
                else -> {

                }
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = bottomSheetState,
        sheetContent = {
            when(uiState.action) {
                Action.ADVANCED -> {
                    Box(modifier = Modifier.padding(MaterialTheme.spacing.space12)) {
                        Text(text = "Hello world")
                    }
                }
                else -> {
                    Box(modifier = Modifier.padding(MaterialTheme.spacing.space12)) {
                        Text(text = uiState.toString())
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                EasyAppBar(title = "Send $slug") {
                    navigateUp.invoke()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier.size(MaterialTheme.spacing.spaceExtraLarge),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sendViewModel.sendingState.assetInfo?.icon)
                            .transformations(CircleCropTransformation()).build(),
                        contentDescription = null
                    )
                    Text(text = "Enter Amount")
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium
                            )
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        value = uiState.amount,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        onValueChange = {
                            sendViewModel.onAmountChanged(it)
                        })
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            keyboardController?.hide()
                            sendViewModel.onActionChanged(Action.ADVANCED)
                            scope.launch {
                                bottomSheetState.show()
                            }
                        }
                    ) {
                        Text(text = "Advanced")
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                modifier = Modifier.weight(1F),
                                value = uiState.toAddress,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                    }
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                onValueChange = {
                                    sendViewModel.onToAddressChanged(it)
                                })
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable {

                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(
                                            end = MaterialTheme.spacing.space12,
                                            start = MaterialTheme.spacing.space12
                                        )
                                        .size(MaterialTheme.spacing.spaceLarge),
                                    imageVector = Icons.Filled.QrCode,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium
                            ),
                        onClick = {
                            keyboardController?.hide()
                            sendViewModel.onActionChanged(Action.MODEL_INFO)
                            sendViewModel.onSign()
                        }
                    ) {
                        Text(text = "Send")
                    }
                }
            }
        }
    }
}
