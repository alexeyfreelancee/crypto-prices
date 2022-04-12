package com.eatmybrain.cryptoprices.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eatmybrain.cryptoprices.data.structures.CandlesData


@Composable
fun CandlesGraph(candlesData:CandlesData){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = candlesData.s)
    }
}