package com.easy.intro.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = introInfo.tips,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp, 12.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
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
