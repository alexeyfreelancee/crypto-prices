package com.eatmybrain.cryptoprices.data

import com.eatmybrain.cryptoprices.data.api.CoinmarketApi
import com.eatmybrain.cryptoprices.data.structures.CryptoInfoResponse
import com.eatmybrain.cryptoprices.data.structures.CryptoListResponse
import com.eatmybrain.cryptoprices.data.structures.Status
import javax.inject.Inject


class Repository @Inject constructor(
    private val api:CoinmarketApi
) {

    suspend fun loadCryptoList() : CryptoListResponse {
        return try {
            api.topCryptoList()
        }catch (ex:Exception){
            ex.printStackTrace()
            CryptoListResponse(status = Status(400, "Request error"))
        }

    }

    suspend fun loadCryptoInfo(symbol:String) : CryptoInfoResponse {
        return try {
            api.cryptoInfo(symbol = symbol)
        }catch (ex:Exception){
            ex.printStackTrace()
            CryptoInfoResponse(status = Status(400, "Request error"))
        }
    }
}