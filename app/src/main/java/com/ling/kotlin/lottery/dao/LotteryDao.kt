//package com.ling.kotlin.lottery.dao
//
//import androidx.room.*
//
//import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
//
//@Dao
//interface LotteryDao {
//
//    @Query("SELECT * FROM lotterygroupinfoentity")
//    fun getAllLotterys():List<LotteryGroupInfoEntity>
//
//    @Query("SELECT * FROM lotterygroupinfoentity WHERE lotteryId = :lotteryId")
//    fun getLotteryById(lotteryId:Int):LotteryGroupInfoEntity
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertLotteryEntity(lottertEntity: LotteryGroupInfoEntity)
//
//    @Query("DELETE FROM lotterygroupinfoentity WHERE lotteryId = :lotteryId")
//    fun  deleteLotteryEntity(lotteryId: Int)
//
//    @Delete
//    fun deleteLotteryEntity(lottertEntity: LotteryGroupInfoEntity)
//}