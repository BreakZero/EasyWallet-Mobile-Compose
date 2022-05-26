package com.easy.assets.presentation.send

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.core.common.Navigator
import com.easy.core.common.UiEvent
import com.easy.core.ui.GlobalRouter
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
    savedStateHandle: SavedStateHandle?,
    sendViewModel: SendingViewModel = assetSendViewModel(slug),
    onBackPressed: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val uiState = sendViewModel.sendingState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    savedStateHandle?.also {
        val backResult =
            it.getLiveData<String>("QR_CODE_CONTENT").asFlow().collectAsState(initial = "")
        LaunchedEffect(key1 = backResult) {
            sendViewModel.onEvent(SendingFormEvent.AddressChanged(backResult.value))
            it.remove<String>("QR_CODE_CONTENT")
        }
    }

    LaunchedEffect(key1 = null) {
        sendViewModel.uiEvent.collect {
            when (it) {
                UiEvent.Success -> {
                    bottomSheetState.show()
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        it.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> {
                    onBackPressed.invoke()
                }
                else -> Unit
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxSize(),
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp
        ),
        sheetContent = {
            when (uiState.action) {
                Action.ADVANCED -> {
                    Box(
                        modifier = Modifier
                    ) {
                        Text(text = "Hello world")
                    }
                }
                else -> {
                    Column(modifier = Modifier.padding(MaterialTheme.spacing.space12)) {
                        Text(text = uiState.toString())
                        FilledTonalButton(
                            modifier = Modifier
                                .padding(top = MaterialTheme.spacing.spaceSmall)
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.spacing.spaceMedium,
                                    end = MaterialTheme.spacing.spaceMedium
                                ),
                            onClick = {
                                scope.launch {
                                    bottomSheetState.hide()
                                }
                                sendViewModel.onEvent(SendingFormEvent.Broadcast)
                            }
                        ) {
                            Text(
                                text = "Broadcast"
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            modifier = Modifier
                .navigationBarsWithImePadding()
                .fillMaxSize(),
            topBar = {
                EasyAppBar(title = "Send $slug") {
                    onBackPressed()
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
                    TextField(
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
                        isError = uiState.amountError != null,
                        onValueChange = {
                            sendViewModel.onEvent(SendingFormEvent.AmountChanged(it))
                        })
                    if (uiState.amountError != null) {
                        Text(
                            text = uiState.amountError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.spaceExtraSmall)
                            .clickable {
                                keyboardController?.hide()
                                sendViewModel.onEvent(SendingFormEvent.ActionChanged(Action.ADVANCED))
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Advanced")
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                    }

                    if (uiState.addressError != null) {
                        Text(
                            modifier = Modifier.padding(MaterialTheme.spacing.spaceLarge),
                            text = uiState.addressError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Card(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.spaceSmall)
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
                                isError = uiState.addressError != null,
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
                                    sendViewModel.onEvent(SendingFormEvent.AddressChanged(it))
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .clickable {
                                        onNavigateTo.invoke(Navigator(GlobalRouter.GLOBAL_SCAN))
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
                            .padding(top = MaterialTheme.spacing.spaceSmall)
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium
                            ),
                        onClick = {
                            keyboardController?.hide()
                            sendViewModel.onEvent(SendingFormEvent.ActionChanged(Action.MODEL_INFO))
                            sendViewModel.onEvent(SendingFormEvent.Submit)
                        }
                    ) {
                        Text(text = "Send")
                    }
                }
            }
        }
    }
}
