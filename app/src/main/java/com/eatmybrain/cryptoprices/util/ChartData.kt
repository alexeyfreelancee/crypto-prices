package com.eatmybrain.cryptoprices.util

import com.eatmybrain.cryptoprices.data.enums.PricePeriod
import com.eatmybrain.cryptoprices.data.structures.OhlcData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ChartData {

    suspend fun parse(ohlcData: OhlcData): LineDataSet = withContext(Dispatchers.IO) {
        val list = ArrayList<Entry>()

        ohlcData.timestampList.forEachIndexed { index, timestamp ->
            val price = ohlcData.closePrices[index]
            val entry = Entry(index.toFloat(), price)
            list.add(entry)
        }
        LineDataSet(list, "Price $")
    }


    fun resolution(period: PricePeriod) :String{
        return when(period) {
            PricePeriod.Today -> "5"
            PricePeriod.Week -> "15"
            PricePeriod.Month -> "60"
            PricePeriod.Year -> "D"
        }
    }


}