//package com.ling.kotlin.lottery.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.ling.kotlin.lottery.bean.LotteryEntity
//import com.ling.kotlin.lottery.dao.LotteryDao
//
//
//@Database(entities = arrayOf(LotteryEntity::class),version = 1,exportSchema = false)
//abstract class LotteryDatabase : RoomDatabase(){
//
//    abstract  fun lotteryDao():LotteryDao
//
//    companion object{
//        private var instance:LotteryDatabase?= null
//        fun  getInstance(context: Context) =
//                instance ?: synchronized(this){
//                    instance ?: Room.databaseBuilder(context.applicationContext,LotteryDatabase::class.java,"lotterys").build().also { instance = it }
//                }
//        fun destroyInstance(){
//            instance = null
//        }
//    }
//}