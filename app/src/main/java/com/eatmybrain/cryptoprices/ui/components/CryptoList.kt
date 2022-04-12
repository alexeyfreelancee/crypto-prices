package com.eatmybrain.cryptoprices.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.eatmybrain.cryptoprices.RoundedCorner
import com.eatmybrain.cryptoprices.data.structures.CryptoItemInfo
import com.eatmybrain.cryptoprices.ui.theme.Green
import com.eatmybrain.cryptoprices.ui.theme.Red
import com.eatmybrain.cryptoprices.util.round

@Composable
fun CryptoLazyColumn(listItems: List<CryptoItemInfo>, onItemClicked: (CryptoItemInfo) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp)) {
        items(items = listItems) { item ->
            CryptoItem(
                cryptoItemInfo = item,
                onItemClicked = onItemClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CryptoItem(
    cryptoItemInfo: CryptoItemInfo,
    onItemClicked: (CryptoItemInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(RoundedCorner),
        onClick = { onItemClicked(cryptoItemInfo) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CryptoImage(
                imageUrl = cryptoItemInfo.imageUrl,
                modifier = Modifier.padding(8.dp),
                size = 42.dp
            )
            Text(
                text = cryptoItemInfo.name,
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.h6
            )

            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                var cryptoPrice =
                    cryptoItemInfo.price.usd.value.round(2).toString().replace(".", ",")
                val percentChange = cryptoItemInfo.price.usd.percentChange24h
                val background = if (percentChange > 0) Green else Red
                val changeSign = if (percentChange > 0) "+" else ""
                val priceChange = percentChange.round(2).toString().replace(".", ",")
                if (cryptoPrice.substringAfter(",") == "0") {
                    cryptoPrice = cryptoPrice.substringBefore(",")
                }


                Text(
                    text = "\$$cryptoPrice",
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color.White,
                        text = "$changeSign$priceChange%",
                        modifier = Modifier.padding(
                            start = 10.dp,
                            end = 4.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoImage(imageUrl: String, size: Dp, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()

    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )
    val state = painter.state


    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
        Box(
            modifier
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = alpha))
                .size(size)
        )
    } else {
        Image(
            painter = painter,
            contentDescription = "Crypto logo",
            modifier = modifier
                .clip(CircleShape)
                .size(size)
        )
    }

}

@Composable
fun LoadingError() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading error ")
    }
}