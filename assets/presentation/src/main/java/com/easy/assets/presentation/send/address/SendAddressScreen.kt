package com.easy.assets.presentation.send.address

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.assets.presentation.detail.AssetDetailViewModel
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.assets.presentation.send.SendInfo
import com.easy.assets.presentation.send.SendInfoHolder
import com.easy.core.common.Navigator
import com.easy.core.ui.LocalSpacing
import com.google.accompanist.insets.statusBarsPadding
import dagger.hilt.android.EntryPointAccessors
import logcat.logcat

@Composable
fun assetSendAddressViewModel(
    slug: String
): SendAddressViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).assetSendAddressViewModelFactory()

    return viewModel(
        factory = SendAddressViewModel.provideFactory(
            factory,
            slug
        )
    )
}

@Composable
fun NormalSendAddressScreen(
    slug: String,
    sendViewModel: SendAddressViewModel = assetSendAddressViewModel(slug),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
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
                title = {
                    Text(text = "Send")
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(
                                id =
                                com.easy.core.ui.R.drawable.ic_scan_white
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .padding(start = 16.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = sendViewModel.sendFlowState.assetInfo?.icon,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = sendViewModel.sendFlowState.assetInfo?.symbol.orEmpty(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "To:", modifier = Modifier.padding(bottom = 8.dp))
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .padding(2.dp)
                            .drawBehind {
                                val strokeWidth = 1.dp.value * density
                                val y = size.height - strokeWidth / 2
                                drawLine(
                                    Color.Green,
                                    Offset(0f, y),
                                    Offset(size.width, y),
                                    strokeWidth
                                )
                            }
                            .padding(top = 4.dp, bottom = 4.dp),
                        value = sendViewModel.sendFlowState.toAddress.orEmpty(),
                        onValueChange = {
                            sendViewModel.onAddressChanged(it)
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            SendInfoHolder.submitField(SendInfo.Field.ADDRESS, sendViewModel.sendFlowState.toAddress.orEmpty())
                            onNavigateTo(Navigator(router = AssetRouter.SEND_SECOND))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Next")
                    }
                }
            }
        }
    }
}