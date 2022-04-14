package com.eatmybrain.cryptoprices.data

import com.eatmybrain.cryptoprices.data.api.CoinmarketApi
import com.eatmybrain.cryptoprices.data.api.FinnhubApi
import com.eatmybrain.cryptoprices.data.structures.CryptoInfoResponse
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import com.eatmybrain.cryptoprices.data.structures.OhlcData
import com.eatmybrain.cryptoprices.data.structures.Status
import javax.inject.Inject


class Repository @Inject constructor(
    private val coinmarketApi: CoinmarketApi,
    private val finnhubApi: FinnhubApi
) {
    suspend fun ohlcData(symbol: String, resolution:String,  from: Long, to: Long): OhlcData? {
        return try {
            finnhubApi.ohlcData(
                symbol = "BINANCE:${symbol}USDT",
                resolution = resolution,
                from = from,
                to = to
            )
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            null
        }
    }

    suspend fun cryptoList(): CryptoListResponse {
        return try {
            val response = coinmarketApi.topCryptoList()
            response.data?.forEach {
                it.imageUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/${it.id}.png"
            }
            response
        } catch (ex: Exception) {
            ex.printStackTrace()
            CryptoListResponse(status = Status(400, "Request error"))
        }

    }

    suspend fun cryptoInfo(symbol: String): CryptoInfoResponse {
        return try {
            val response = coinmarketApi.cryptoInfo(symbol = symbol)
            response.data!![symbol]?.apply {
                imageUrl = "https://s2.coinmarketcap.com/static/img/coins/128x128/${this.id}.png"
            }
            response
        } catch (ex: Exception) {
            ex.printStackTrace()
            CryptoInfoResponse(status = Status(400, "Request error"))
        }
    }
}