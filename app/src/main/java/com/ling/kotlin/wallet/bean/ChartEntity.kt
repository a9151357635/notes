package com.ling.kotlin.wallet.bean



data class ChartEntity(
    var currentMonths: List<CurrentMonth>,
    var lastMonths: List<LastMonth>,
    var total: Float,
    var type: String,
    var maxRange:Float,
    var minRange:Float,
    var lastLineName:String,
    var currentLineName:String
)

data class CurrentMonth(
    var date: String,
    var label: Float
)

data class LastMonth(
    var date: String,
    var label: Float
)