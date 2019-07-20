package com.ling.kotlin.lottery.bean


data class PeriodTimeEntity(val lotteryId:Int,val curPeriodNum:Long,val remainTime:Long,val blockTime:Long,val sysTime:Long,val openTime:Long,var convertRemainTime:Long,val openResult:OpenResult)

data class OpenResult(val opencode:String,val opentime:String,val openPeriod:Long,val zodiac:String,val resultColor:String)