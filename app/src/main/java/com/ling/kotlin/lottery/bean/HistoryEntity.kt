package com.ling.kotlin.lottery.bean

import android.text.TextUtils
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import java.util.*

data class HistoryEntity(val zodiac:String,
                         val resultColor: String,
                         val lotteryID:Int,
                         val expectNumber:String,
                         val openCode:String,
                         val openTime:String,
                         val itemTypes:Int = NORMAL) : AbstractExpandableItem<HistoryEntity>(),MultiItemEntity{

    override fun getLevel(): Int = 1

    override fun getItemType(): Int = itemTypes

    companion object{
        const val COUNT_DOWN = 2
        const val NORMAL = 4
        fun getZodiacList(zodiac: String): List<String> {
            return arrayToList(zodiac)
        }

        fun getOpenNumber(openCode: String): List<String> {
            return arrayToList(openCode)
        }

        fun getResultColor(resultColor: String): List<String> {
            return arrayToList(resultColor)
        }
        private fun arrayToList(result: String): List<String> {
            val resultList = ArrayList<String>()
            if (TextUtils.isEmpty(result)) {
                return resultList
            }
            if (result.contains(",")) {
                resultList.addAll(listOf(*result.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
            }
            if (resultList.size > 0) {
                val lastItem = resultList.removeAt(resultList.size - 1)
                if (lastItem.contains("+")) {
                    resultList.addAll(listOf(*lastItem.split("\\+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
                    //最后的前一个加入要给识别
                    resultList.add(resultList.size - 1, "add")
                } else {
                    resultList.add(lastItem)
                }
            }
            return resultList
        }
    }
}