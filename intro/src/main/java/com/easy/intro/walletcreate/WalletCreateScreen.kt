package com.easy.intro.walletcreate

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.common.Navigator
import com.easy.core.ui.components.EasyAppBar
import com.easy.intro.IntroRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletCreateScreen(
    viewModel: WalletCreateViewModel = hiltViewModel(),
    onNavigate: (Navigator) -> Unit,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            EasyAppBar {
                onNavigateUp()
            }
        }
    ) {
        val checked = viewModel.state
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Legal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Text(
                    text = "Please review the EasyWallet Terms of Service and Privacy Policy.",
                    modifier = Modifier.padding(start = 16.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                        .border(
                            width = 0.1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Terms of Service", modifier = Modifier.padding(start = 12.dp))
                        Icon(
                            modifier = Modifier.padding(end = 12.dp),
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = null
                        )
                    }
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Privacy Policy", modifier = Modifier.padding(start = 12.dp))
                        Icon(
                            imageVector = Icons.Filled.ArrowRight,
                            contentDescription = null, modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                }

                Row {
                    Checkbox(checked = checked, onCheckedChange = {
                        viewModel.toggle(it)
                    })
                    Text(
                        text = "I???ve read and accept the Terms of Service and Privacy Policy",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(4.dp),
                enabled = checked,
                onClick = {
                    onNavigate.invoke(Navigator(router = IntroRouter.ROUTER_WALLET_PROTECT))
                }
            ) {
                Text(text = "Continue")
            }
        }
    }
}
