package com.easy.intro.walletsetup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletSetupScreen(
    title: String,
    onNavigateTo: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            EasyAppBar(
                title = title
            ) {
                // navController.navigateUp()
                onNavigateTo.invoke("back")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(
                    start = MaterialTheme.spacing.spaceMedium,
                    end = MaterialTheme.spacing.spaceMedium,
                    bottom = MaterialTheme.spacing.spaceMedium
                )
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = MaterialTheme.spacing.spaceLarge),
                text = "Wallet setup"
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Import an existing wallet or create a new one"
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(24.dp),
                onClick = {
                    // navController.navigate(Routers.ROUTER_WALLET_CREATE)
                    onNavigateTo.invoke("import a wallet")
                }
            ) {
                Text(text = "Import using Secret Recovery Phrase")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(24.dp),
                onClick = {
                    // create wallet
                    onNavigateTo.invoke("to wallet create")
                }
            ) {
                Text(text = "Create a new wallet")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "Term",
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
    }
}
