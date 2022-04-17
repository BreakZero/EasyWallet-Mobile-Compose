package com.easy.intro.walletprotect

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.common.Navigator
import com.easy.core.common.UiEvent
import com.easy.core.ui.LocalSpacing
import com.easy.core.ui.common.BiometricUtil
import com.easy.intro.IntroRouter
import com.easy.intro.R

@Composable
fun WalletProtectScreen(
    viewModel: WalletProtectViewModel = hiltViewModel(),
    passcode: String? = null,
    onNavigate: (Navigator) -> Unit,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.clickable { onNavigateUp() }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(LocalSpacing.current.space48)
                            .padding(LocalSpacing.current.spaceSmall),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
                if (passcode.orEmpty().isNotEmpty()) {
                    Box(modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                        .height(48.dp)
                        .clickable {
                            viewModel.onEvent(ProtectEvent.OnCreated)
                        }, contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                            text = "Next"
                        )
                    }
                }
            }
        }
    ) {
        passcode?.run {
            viewModel.initPasscode(this)
        }
        val context = LocalContext.current
        LaunchedEffect(key1 = null) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Success -> {
                        onNavigate.invoke(Navigator(IntroRouter.ROUTER_MAIN))
                    }
                    is UiEvent.ShowSnackbar -> {
                        Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Protect your wallet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp),
                text = "Below settings are shared across multi-wallets"
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                painter = painterResource(id = R.mipmap.banner_welcome),
                contentDescription = null
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp)
                    .height(64.dp)
                    .clickable {
                        onNavigate.invoke(Navigator(router = IntroRouter.ROUTER_PASSCODE))
                    },
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Create Passcode", modifier = Modifier.padding(start = 12.dp))
                        Text(
                            text = "You're protected",
                            color = if (viewModel.state.passcode.isEmpty()) Color.Gray else Color.Green,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = null, modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp)
                    .height(64.dp),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Enable Biometric", modifier = Modifier.padding(start = 12.dp))
                        Text(text = "Recommended", modifier = Modifier.padding(start = 12.dp))
                    }
                    Switch(
                        checked = viewModel.state.biometric,
                        onCheckedChange = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                BiometricUtil.launchBiometric(context,
                                    @RequiresApi(Build.VERSION_CODES.P)
                                    object : BiometricPrompt.AuthenticationCallback() {
                                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                                            super.onAuthenticationSucceeded(result)
                                            viewModel.onEvent(
                                                ProtectEvent.OnBiometricChanged(
                                                    error = "",
                                                    enabled = it
                                                )
                                            )
                                        }

                                        override fun onAuthenticationError(
                                            errorCode: Int,
                                            errString: CharSequence?
                                        ) {
                                            super.onAuthenticationError(errorCode, errString)
                                            viewModel.onEvent(
                                                ProtectEvent.OnBiometricChanged(
                                                    error = "Authentication Error code: $errorCode",
                                                    enabled = it.not()
                                                )
                                            )
                                        }

                                        override fun onAuthenticationHelp(
                                            helpCode: Int,
                                            helpString: CharSequence?
                                        ) {
                                            super.onAuthenticationHelp(helpCode, helpString)
                                        }
                                    }, onCancel = {
                                        viewModel.onEvent(
                                            ProtectEvent.OnBiometricChanged(
                                                error = "user canceled",
                                                enabled = it.not()
                                            )
                                        )
                                    })
                            } else {
                                viewModel.onEvent(
                                    ProtectEvent.OnBiometricChanged(
                                        error = "device unsupport",
                                        enabled = false
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(end = 12.dp)
                    )
                }
            }
        }
    }
}