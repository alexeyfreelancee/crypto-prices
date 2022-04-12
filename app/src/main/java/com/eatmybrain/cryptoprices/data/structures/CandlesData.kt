package com.eatmybrain.cryptoprices.data.structures


data class CandlesData(
    val c: List<Double>,
    val h: List<Double>,
    val l: List<Double>,
    val o: List<Double>,
    val t: List<Double>,
    val v: List<Double>,
    val s:String
)