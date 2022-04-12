package com.eatmybrain.cryptoprices.util

enum class CryptoAppTab {
    Crypto,
    DeFi,
    NFT,
    Metaverse;

    companion object {
        fun fromTitle(route:String?) : CryptoAppTab {
            return when(route?.substringBefore("/")){
                Crypto.name -> Crypto
                DeFi.name -> DeFi
                NFT.name -> NFT
                Metaverse.name -> Metaverse
                null -> Crypto
                else -> throw IllegalStateException("Route $route is not recognized")
            }
        }
    }
}