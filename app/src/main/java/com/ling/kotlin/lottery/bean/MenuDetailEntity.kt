package com.ling.kotlin.lottery.bean

data class MenuDetailEntity(
    val gamePlays: Any,
    val id: String,
    val isHot: Int,
    val lotteryId: Int,
    val menuDetails: Any,
    val menuName: String,
    val sort: Int
)