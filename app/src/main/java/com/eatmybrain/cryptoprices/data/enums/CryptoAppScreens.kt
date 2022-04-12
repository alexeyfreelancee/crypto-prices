package com.eatmybrain.cryptoprices.data.enums

enum class CryptoAppScreens {
    CryptoList,
    CryptoInfo;


    companion object {
        fun fromRoute(route:String?) : CryptoAppScreens {
            return when(route?.substringBefore("/")){
                CryptoList.name -> CryptoList
                CryptoInfo.name -> CryptoInfo
                null -> CryptoList
                else -> throw IllegalStateException("Route $route is not recognized")
            }
        }
    }
}