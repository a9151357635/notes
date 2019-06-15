package com.ling.kotlin.wallet.bean

data class WalletMyEntity(
    val happyMoney: String,
    val happyIncome: String,
    val kyMoney: String,
    val kyIncome: String,
    val todayMoney: String,
    val totalMoney: String,
    val walletHistory: List<WalletHistoryEntity>
)