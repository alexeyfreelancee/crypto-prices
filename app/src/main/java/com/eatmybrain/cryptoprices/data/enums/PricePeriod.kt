package com.eatmybrain.cryptoprices.data.enums

enum class PricePeriod {
    Today,
    Week,
    Month,
    Year;


    companion object {
        fun textValues() = listOf("24h", "7 days", "30 days", "1 year")
        fun fromText(text:String) : PricePeriod {
           return when(text) {
                "24h" -> Today
                "7 days" -> Week
                "30 days" -> Month
                "1 year" -> Year
                else -> throw IllegalStateException("Unknown text value")
            }
        }
    }
}