package com.easy.intro.intro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.easy.core.ui.spacing
import com.easy.intro.components.IntroPager
import com.easy.intro.model.IntroInfo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroMainScreen(
    title: String,
    intros: List<IntroInfo>,
    onNavigateTo: (String) -> Unit
) {
    val pagerState = rememberPagerState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = MaterialTheme.spacing.spaceMedium),
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineMedium
            )
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MaterialTheme.spacing.spaceLarge)
            ) { page ->
                IntroPager(introInfo = intros[page])
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(MaterialTheme.spacing.space12)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
            OutlinedButton(
                onClick = {
                    onNavigateTo.invoke("to wallet setup")
                },
                shape = RoundedCornerShape(MaterialTheme.spacing.spaceExtraLarge),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.spaceExtraLarge)
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        bottom = MaterialTheme.spacing.space12,
                        start = MaterialTheme.spacing.spaceMedium,
                        end = MaterialTheme.spacing.space12
                    )
            ) {
                Text(
                    text = "Get started",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))
        }
    }
}
