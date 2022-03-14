package com.easy.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.core.ui.R
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingsScreen(
    title: String,
    navigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = null) {
        systemUIController.setStatusBarColor(color = Color.White, darkIcons = true)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                title = title,
                backgroundColor = Color.White
            ) {
                navigateUp()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.avatar_generic_1),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(56.dp)
            )
            Text(
                text = "Wallet Name",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "Security", fontSize = 12.sp, color = Color.Gray)
            Card(
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Protect Your Wallet",
                        subTitle = "Passcode, Biometrics and 2FA"
                    ) {

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.2.dp)
                    )
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Recovery Phrase",
                        subTitle = "My Wallet"
                    ) {

                    }
                }
            }
            Text(text = "About EasyWallet", fontSize = 12.sp, color = Color.Gray)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 56.dp)
                            .padding(start = 12.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Version")
                        Text(text = "1.29.0-st", color = Color.Gray)
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.2.dp)
                    )
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Terms of Service"
                    ) {

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.2.dp)
                    )
                    MenuItemView(modifier = Modifier.fillMaxWidth(), title = "Privacy Notice") {

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.2.dp)
                    )
                    MenuItemView(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Visit our website"
                    ) {

                    }
                    }
                }
        }
    }
}

@Composable
private fun MenuItemView(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String? = null,
    action: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                action.invoke()
            }
            .padding(start = 12.dp, end = 12.dp)
            .defaultMinSize(minHeight = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(text = title)
            subTitle?.let {
                Text(text = subTitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = Icons.Filled.ArrowRight,
            contentDescription = null
        )
    }
}