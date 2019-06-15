package com.ling.kotlin.wallet.bean

data class WalletEntity(
    val availableMoney: String,
    val todayMoney: String,
    val totalMoney: String,
    val unavailableMoney: String,
    val walletHistory: List<WalletHistoryEntity>
)