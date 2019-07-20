package com.ling.kotlin.lottery.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

data class LotteryGroupInfoEntity(
    val lotteryId: Int,
    val playId: String,
    val name: String,
    val odds: String,
    val number: Int,
    val isNumber: Int,
    val datas: List<LotteryInfoEntity>,
    val remark: String,
    var itemType: Int
) : Serializable {
    companion object {
        const val DOUBLE = 1
        const val NUMBER = 2
        const val SFY = 3
        const val MARK_SIX = 4
        const val K_THREE = 5
    }
}

data class LotteryInfoEntity(
    val lotteryId: Int,
    val playId: String,
    val odds: String,
    val name: String,
    val isNumber: Int,
    val number: Int,
    val remark: String,
    val color: String,
    val belongNumbers: String,
    var convertPlayId: String,
    var convertNumber: List<String>,
    var convertName: String,
    var type: Int = NORMAL,
    var selectId: Int,
    var convertOdds: String
) : Serializable, MultiItemEntity {
    companion object {
        const val NORMAL = 10
        const val MARKSIX = 11
        const val ELEVEN = 12
        const val WELFARE = 13
    }

    override fun getItemType(): Int = type
}
