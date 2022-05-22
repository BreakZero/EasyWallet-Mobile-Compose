package com.easy.assets.presentation.assets.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easy.core.ui.spacing

@Composable
fun IconWithLabel(
    imageVector: ImageVector,
    @StringRes labelRes: Int,
    iconTint: Color,
    textColor: Color = Color.Blue,
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .clickable {
            onClick.invoke()
        }) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(MaterialTheme.spacing.space48)
                    .width(MaterialTheme.spacing.space48)
                    .align(Alignment.CenterHorizontally)
                    .background(iconTint, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    imageVector = imageVector,
                    tint = textColor,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                color = textColor,
                text = stringResource(id = labelRes),
                textAlign = TextAlign.Center
            )
        }
    }
}