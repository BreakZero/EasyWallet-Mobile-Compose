package com.easy.intro.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easy.intro.model.IntroInfo

@Composable
fun IntroPager(
    modifier: Modifier = Modifier,
    introInfo: IntroInfo
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = introInfo.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = introInfo.tips,
            fontSize = 16.sp,
            fontWeight = FontWeight.W300,
            modifier = Modifier
                .padding(16.dp, 12.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            painter = painterResource(id = introInfo.iconRes),
            contentDescription = null,
            alignment = Alignment.Center
        )
    }
}
