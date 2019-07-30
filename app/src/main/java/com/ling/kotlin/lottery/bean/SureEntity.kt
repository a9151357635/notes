package com.ling.kotlin.lottery.bean

data class SureEntity(var lotteryId:String,var PeriodNumber:String,var lotteryName:String,var data:List<SureInfoEntity>)

data class SureInfoEntity(var playId:String,var playName:String,var betInfo:String,var money:Double,var totalMoney:String,var orderdetail:String,var odds:String,var ZuShu:Int)