package com.ling.kotlin.lottery.bean

/**
 * 未结注单实体
 */
data class OpenNoteEntity(
    val items: List<OpenNoteInfoEntity>,
    val pageIndex: Int,
    val pageSize: Int,
    val total: Int,
    val totalPage: Int
)

/**
 * 未结注单分页实体
 */
data class OpenNoteInfoEntity(
    val id: Int,
    val betMoney: Double,
    val betOzd:String,
    val betOdds: String,
    val betTime: String,
    val detail: String,
    val lotteryID: Int,
    val lotteryName: String,
    val periods: String,
    val playId: Int,
    val userName: String
)