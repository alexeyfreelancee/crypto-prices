package com.eatmybrain.cryptoprices.ui.crypto_info

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eatmybrain.cryptoprices.MainActivity
import com.eatmybrain.cryptoprices.data.enums.PricePeriod
import com.eatmybrain.cryptoprices.data.structures.CryptoFullInfo
import com.eatmybrain.cryptoprices.ui.components.CryptoImage
import com.eatmybrain.cryptoprices.ui.components.CryptoTabsRow
import com.eatmybrain.cryptoprices.ui.components.LinkifyText
import com.eatmybrain.cryptoprices.ui.components.LoadingError
import com.eatmybrain.cryptoprices.util.ResultOf
import com.eatmybrain.cryptoprices.util.doIfSuccess
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.EntryPointAccessors


@Composable
fun CryptoInfoScreen(
    cryptoSymbol: String,
    viewModel: CryptoInfoViewModel = cryptoInfoViewModel(cryptoSymbol)
) {
    val cryptoInfoResult by viewModel.cryptoInfo.observeAsState()
    val lineChartDataResult by viewModel.chartData.observeAsState()
    val refreshing by viewModel.isRefreshing.observeAsState()

    if (refreshing == true) {
        Loading()
    } else {
        ScreenContent(
            cryptoInfoResult = cryptoInfoResult,
            onPeriodChanged = {
                viewModel.updateChartPeriod(it)
            },
            chartDataResult = lineChartDataResult
        )
    }
}

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ScreenContent(
    chartDataResult: ResultOf<LineDataSet>?,
    cryptoInfoResult: ResultOf<CryptoFullInfo>?,
    onPeriodChanged: (String) -> Unit
) {
    Column {
        if (cryptoInfoResult is ResultOf.Failure) {
            LoadingError()
        } else {
            cryptoInfoResult?.doIfSuccess { cryptoInfo -> Header(cryptoInfo) }
            chartDataResult?.doIfSuccess { lineChartData ->
                PricesHistoryChart(
                    onPeriodChanged,
                    lineChartData
                )
            }
        }
    }
}


@Composable
fun PricesHistoryChart(onPeriodChanged: (String) -> Unit, chartData: LineDataSet) {
    val allTabs = PricePeriod.textValues()
    var currentTab by remember { mutableStateOf(allTabs.first()) }
    val lineData = LineData(chartData.apply {
        color = MaterialTheme.colors.primary.toArgb()
        valueTextSize = 0f
        setDrawCircles(false)
        setDrawCircleHole(false)


    })

    Column {
        CryptoTabsRow(
            allTabs = allTabs,
            currentTab = currentTab,
            onItemClicked = { periodText ->
                currentTab = periodText
                onPeriodChanged(periodText)
            }
        )

        AndroidView(
            factory = {
                LineChart(it).apply {
                    id = View.generateViewId()
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    axisLeft.textColor = Color.White.toArgb()
                    axisRight.textColor = Color.White.toArgb()
                    xAxis.textColor = Color.White.toArgb()
                    legend.textColor = Color.White.toArgb()
                    description.isEnabled = false
                    setGridBackgroundColor(Color.White.toArgb())
                    data = lineData
                    invalidate()
                }
            },
            modifier = Modifier.padding(16.dp),
            update = { lineChart ->
                lineChart.apply {
                    data = lineData
                    invalidate()
                }
            }
        )
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

