package com.eatmybrain.cryptoprices.data.structures

import com.google.gson.annotations.SerializedName

data class CryptoListResponse(val status:Status, val data:List<CryptoItemInfo>? = null)

data class Status(
    @SerializedName("error_code")
    val errorCode:Int,
    @SerializedName("error_message")
    val errorMessage:String?
)

data class CryptoItemInfo(
    val id:Int,
    var imageUrl:String,
    val name:String,
    @SerializedName("quote")
    val price:Price,
    val symbol:String,
    val tags:List<String> = emptyList()
)

data class Price(
    @SerializedName("USD")
    val usd:USD
)

data class USD(
    @SerializedName("price")
    val value: Float,
    @SerializedName("percent_change_24h")
    val percentChange24h:Float
)

