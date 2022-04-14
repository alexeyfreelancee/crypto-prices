package com.eatmybrain.cryptoprices.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eatmybrain.cryptoprices.util.Constants

@Composable
fun CryptoTabsRow(
    allTabs: List<String>,
    currentTab: String,
    onItemClicked: (String) -> Unit,
    modifier :Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Constants.TabsRowHeight)
            .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        allTabs.forEach { screen ->
            TabItem(
                name = screen,
                selected = screen == currentTab,
                onItemClicked = onItemClicked
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabItem(name: String, selected: Boolean, onItemClicked: (String) -> Unit) {
    val animSpec = remember {
        tween<Color>(
            durationMillis = if(selected) 150 else 100,
            easing = LinearEasing
        )
    }
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(alpha = 0.4f),
        animationSpec = animSpec
    )

    Card(
        backgroundColor = backgroundColor,
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(Constants.RoundedCorner),
        onClick = { onItemClicked(name) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ){
            Text(
                text = name,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                color = MaterialTheme.colors.onSurface
            )
        }

    }
}

