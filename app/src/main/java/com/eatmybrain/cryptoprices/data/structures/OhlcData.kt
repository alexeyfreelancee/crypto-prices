package com.eatmybrain.cryptoprices.data.structures

import com.google.gson.annotations.SerializedName


data class OhlcData(
    @SerializedName("c")
    val closePrices: List<Float>,
    @SerializedName("h")
    val highPrices: List<Float>,
    @SerializedName("l")
    val lowPrices: List<Float>,
    @SerializedName("o")
    val openPrices: List<Float>,
    @SerializedName("t")
    val timestampList: List<Long>,
    @SerializedName("v")
    val volumeData: List<Float>,
    @SerializedName("s")
    val status:String
)