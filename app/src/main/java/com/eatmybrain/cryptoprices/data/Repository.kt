package com.eatmybrain.cryptoprices.data

import com.eatmybrain.cryptoprices.data.api.CoinmarketApi
import com.eatmybrain.cryptoprices.data.api.FinnhubApi
import com.eatmybrain.cryptoprices.data.structures.CandlesData
import com.eatmybrain.cryptoprices.data.structures.CryptoInfoResponse
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import com.eatmybrain.cryptoprices.data.structures.Status
import javax.inject.Inject


class Repository @Inject constructor(
    private val coinmarketApi:CoinmarketApi,
    private val finnhubApi:FinnhubApi
) {
    suspend fun loadCandlesData(symbol: String, from:Long, to:Long): CandlesData? {
        return try {
            finnhubApi.candlesData(
                symbol = "BINANCE:${symbol}USDT",
                resolution = "D",
                from =from,
                to = to
            )
        } catch (ex:java.lang.Exception){
            ex.printStackTrace()
            null
        }
    }
    suspend fun loadCryptoList() : CryptoListResponse {
        return try {
            coinmarketApi.topCryptoList()
        }catch (ex:Exception){
            ex.printStackTrace()
            CryptoListResponse(status = Status(400, "Request error"))
        }

    }

    suspend fun loadCryptoInfo(symbol:String) : CryptoInfoResponse {
        return try {
            coinmarketApi.cryptoInfo(symbol = symbol)
        }catch (ex:Exception){
            ex.printStackTrace()
            CryptoInfoResponse(status = Status(400, "Request error"))
        }
    }
}