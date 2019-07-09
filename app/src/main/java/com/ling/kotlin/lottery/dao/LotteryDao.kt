//package com.ling.kotlin.lottery.dao
//
//import androidx.room.*
//import com.ling.kotlin.lottery.bean.LotteryEntity
//
//@Dao
//interface LotteryDao {
//
//    @Query("SELECT * FROM lotterys_tab")
//    fun getAllLotterys():List<LotteryEntity>
//
//    @Query("SELECT * FROM lotterys_tab WHERE lotteryId = :lotteryId")
//    fun getLotteryById(lotteryId:Int):LotteryEntity
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertLotteryEntity(lottertEntity: LotteryEntity)
//
//    @Query("UPDATE lotterys_tab SET remainTime =:remainTime WHERE lotteryId =:lotteryId")
//    fun updateLotteryEntity(lotteryId: Int,remainTime:Int)
//
//    @Query("DELETE FROM lotterys_tab WHERE lotteryId = :lotteryId")
//    fun  deleteLotteryEntity(lotteryId: Int)
//
//    @Delete
//    fun deleteLotteryEntity(lottertEntity: LotteryEntity)
//}