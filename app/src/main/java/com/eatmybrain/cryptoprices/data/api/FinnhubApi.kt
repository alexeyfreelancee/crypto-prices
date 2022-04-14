package com.eatmybrain.cryptoprices.data.api

import com.eatmybrain.cryptoprices.BuildConfig
import com.eatmybrain.cryptoprices.data.structures.OhlcData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FinnhubApi {

    //  api/v1/crypto/candle?symbol=BINANCE:BTCUSDT&resolution=D&from=1572651390&to=1575243390
    @GET("api/v1/crypto/candle")
    suspend fun ohlcData(
        @Header("X-Finnhub-Token") apiKey:String = BuildConfig.FINNHUB_KEY,
        @Query("symbol") symbol:String,
        @Query("resolution") resolution:String,
        @Query("from") from:Long,
        @Query("to") to:Long
    ) : OhlcData
}