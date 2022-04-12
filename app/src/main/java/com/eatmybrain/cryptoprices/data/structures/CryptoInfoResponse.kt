package com.eatmybrain.cryptoprices.data.structures

data class CryptoInfoResponse(val status: Status, val data: HashMap<String, CryptoFullInfo>? = null)

data class CryptoFullInfo(
    val name: String,
    val description: String,
    val id: Int,
    var imageUrl: String,
    val urls: HashMap<String, List<String>>
)