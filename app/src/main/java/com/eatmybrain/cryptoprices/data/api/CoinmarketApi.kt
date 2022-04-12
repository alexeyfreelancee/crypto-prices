package com.eatmybrain.cryptoprices.data.api


import com.eatmybrain.cryptoprices.BuildConfig
import com.eatmybrain.cryptoprices.data.structures.CryptoInfoResponse
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinmarketApi {


    @GET("v1/cryptocurrency/listings/latest")
    @Headers(
        "Accept: application/json"
    )
    suspend fun topCryptoList(
        @Header("X-CMC_PRO_API_KEY")
        apiKey:String = BuildConfig.COINMARKETCAP_KEY,
        @Query("start") start:Int = 1,
        @Query("limit") limit:Int = 500
    ) : CryptoListResponse

    @GET("v1/cryptocurrency/info")
    @Headers(
        "Accept: application/json"
    )
    suspend fun cryptoInfo(
        @Header("X-CMC_PRO_API_KEY")
        apiKey:String = BuildConfig.COINMARKETCAP_KEY,
        @Query("symbol")
        symbol:String
    ) : CryptoInfoResponse
}