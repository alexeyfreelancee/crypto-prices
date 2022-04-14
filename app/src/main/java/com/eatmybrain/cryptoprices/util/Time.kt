package com.eatmybrain.cryptoprices.util

import com.eatmybrain.cryptoprices.data.enums.PricePeriod
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Time {
    fun currentTime() = System.currentTimeMillis() / 1000

    fun fromPeriod(period: PricePeriod):Long{
        return when(period){
            PricePeriod.Today -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
            PricePeriod.Month -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            PricePeriod.Week -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
            PricePeriod.Year -> System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365)
        } / 1000
    }

    fun fromTimestamp(timestamp:Long):String{
        val sdf = SimpleDateFormat("dd.MM", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
}