package com.ling.kotlin.lottery.bean


data class LotteryEntity(
    val blockTime: Int,
    val curPeriodNum: Long,
    val isHot: Int,
    val lotteryId: Int,
    val lotteryName: String,
    val menuDetails: List<MenuDetailEntity>,
    val nextTime: String,
    val openResult: Any,
    val remainTime: Int,
    val sysTime: Int,
    var convertRemainTime:Long,
    var isFollow:Boolean
)