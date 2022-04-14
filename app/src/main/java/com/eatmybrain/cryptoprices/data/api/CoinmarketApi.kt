package com.eatmybrain.cryptoprices.data.api


import com.eatmybrain.cryptoprices.BuildConfig
import com.eatmybrain.cryptoprices.data.structures.CryptoInfoResponse
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import com.eatmybrain.cryptoprices.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinmarketApi {


    @GET("v1/cryptocurrency/listings/latest")
    @Headers("Accept: application/json")
    suspend fun topCryptoList(
        @Header("X-CMC_PRO_API_KEY")
        apiKey:String = BuildConfig.COINMARKETCAP_KEY,
        @Query("start") start:Int = Constants.START_LIST_LIMIT,
        @Query("limit") limit:Int = Constants.END_LIST_LIMIT
    ) : CryptoListResponse

    @GET("v1/cryptocurrency/info")
    @Headers("Accept: application/json")
    suspend fun cryptoInfo(
        @Header("X-CMC_PRO_API_KEY")
        apiKey:String = BuildConfig.COINMARKETCAP_KEY,
        @Query("symbol")
        symbol:String
    ) : CryptoInfoResponse
}