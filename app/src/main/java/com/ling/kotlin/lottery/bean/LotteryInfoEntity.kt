package com.ling.kotlin.lottery.bean

import androidx.room.*

data class LotteryGroupInfoEntity (
    @PrimaryKey
    var menuId:String,
    val lotteryId: Int,
    val playId: String,
    val name: String,
    val odds: String,
    val number: Int,
    val isNumber: Int,
    @Embedded
    val datas: List<LotteryInfoEntity>,
    val remark: String,
    var itemType: Int

)  {
    companion object {
        const val DOUBLE = 1
        const val NUMBER = 2
        const val SFY = 3
        const val MARK_SIX = 4
        const val K_THREE = 5
    }
}

data class LotteryInfoEntity (
    @PrimaryKey
    val lotteryId: Int,
    val playId: String,
    val odds: String,
    val name: String,
    val isNumber: Int,
    val number: Int,
    val remark: String,
    val color: String,
    val belongNumbers: String,
    var convertPlayId: String?=null,
    var convertNumber: List<String>,
    var convertName: String ?=null,
    var type: Int = 0,
    var selectId: Int,
    var convertOdds: String,
    var groupPosition: Int,
    var combineSize: Int
){
    companion object {
        //只有一注
        const val ONLY_ONE = 10
    }
}
