package com.eatmybrain.cryptoprices.ui.crypto_info

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eatmybrain.cryptoprices.MainActivity
import com.eatmybrain.cryptoprices.data.structures.CryptoFullInfo
import com.eatmybrain.cryptoprices.ui.components.CandlesGraph
import com.eatmybrain.cryptoprices.ui.components.CryptoImage
import com.eatmybrain.cryptoprices.ui.components.LinkifyText
import com.eatmybrain.cryptoprices.ui.components.LoadingError
import com.eatmybrain.cryptoprices.util.doIfFailure
import com.eatmybrain.cryptoprices.util.doIfSuccess
import dagger.hilt.android.EntryPointAccessors


//TODO price graph
@Composable
fun CryptoInfoScreen(
    cryptoSymbol: String,
    viewModel: CryptoInfoViewModel = cryptoInfoViewModel(cryptoSymbol)
) {
    val cryptoInfoResult by viewModel.cryptoInfo.observeAsState()
    val candlesDataResult by viewModel.candlesData.observeAsState()
    val refreshing by viewModel.isRefreshing.observeAsState()

    if (refreshing == true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    Column {
        cryptoInfoResult?.doIfFailure {
            LoadingError()
        }
        cryptoInfoResult?.doIfSuccess { cryptoInfo ->
            Header(cryptoInfo)
        }

        candlesDataResult?.doIfFailure {
            LoadingError()
        }

        candlesDataResult?.doIfSuccess { candlesData ->
            CandlesGraph(candlesData)
        }
    }

}


@Composable
fun cryptoInfoViewModel(cryptoSymbol: String): CryptoInfoViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java
    ).cryptoInfoViewModelFactory()
    return viewModel(factory = CryptoInfoViewModel.provideFactory(factory, cryptoSymbol))
}


@Composable
fun Header(cryptoInfo: CryptoFullInfo) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CryptoImage(
                imageUrl = cryptoInfo.imageUrl,
                modifier = Modifier.padding(16.dp),
                size = 80.dp
            )
            Column(modifier = Modifier.padding(top = 16.dp, end = 16.dp)) {
                Text(
                    text = cryptoInfo.name,
                    style = MaterialTheme.typography.h6
                )

                val twitterUrl = cryptoInfo.urls["twitter"]?.firstOrNull()
                if (!twitterUrl.isNullOrEmpty()) {
                    LinkifyText(
                        text = twitterUrl,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                val websiteUrl = cryptoInfo.urls["message_board"]?.firstOrNull()
                if (!websiteUrl.isNullOrEmpty()) {
                    LinkifyText(
                        text = websiteUrl,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        LinkifyText(
            text = cryptoInfo.description,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        )
    }
}

