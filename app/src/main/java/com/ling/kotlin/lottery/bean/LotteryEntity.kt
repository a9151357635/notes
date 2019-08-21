package com.ling.kotlin.lottery.bean

import com.google.gson.annotations.SerializedName

data class LotteryEntity(
    val blockTime: Int,
    val curPeriodNum: Long,
    val isHot: Int,
    val lotteryId: Int,
    val lotteryName: String,
    val menuDetails: List<MenuEntity>,
    val nextTime: String,
    val remainTime: Int,
    val sysTime: Int,
    var convertRemainTime: Long,
    var isFollow: Boolean,
    val name:String,
    val code:String,
    val markFlag:Int,
    val subType:String,
    val status:String,
    val iconPath:String,
    @SerializedName(value = "lotteryPeriodInfoResp")val periodInfoEntity:PeriodInfoEntity
)

data class MenuEntity(
    val id: String,
    val isHot: Int,
    val lotteryId: Int,
    val menuDetails: List<MenuChildEntity>,
    val menuName: String,
    val sort: Int,
    var selectId: Int,
    var menuType: Int
)

class MenuChildEntity(val menuName: String, val lotteryId: Int, val id: String, var selectId: Int)

class DrawerEntity(val typeName:String, var type:Int, var data:List<LotteryEntity>)

data class PeriodInfoEntity(val closeTime:Int,val curPeriod:String,val lotteryCode:String)