package com.ling.kotlin.utils

import android.util.SparseIntArray
import com.ling.kotlin.R

object LotteryUtils {

    fun getAppIcon(): SparseIntArray {
        val  iconArray = SparseIntArray()
        iconArray.put(1, R.mipmap.app_chonqing_often_icon)
        iconArray.put(4, R.mipmap.app_xinjiang_often_icon)
        iconArray.put(5, R.mipmap.app_tianjing_often_icon)
        iconArray.put(10, R.mipmap.app_kthree_jiangsu)
        iconArray.put(11, R.mipmap.app_kthree_beijing)
        iconArray.put(12, R.mipmap.app_kthree_guangxi)
        iconArray.put(13, R.mipmap.app_kthree_shanghai)
        iconArray.put(14, R.mipmap.app_kthree_hubei)
        iconArray.put(15, R.mipmap.app_kthree_hebei)
        iconArray.put(50, R.mipmap.app_pk10_icon)
        iconArray.put(55, R.mipmap.app_flybrow_icon)
        iconArray.put(65, R.mipmap.app_beijingkuaile8_icon)
        iconArray.put(66, R.mipmap.app_pc_eggs_icon)
        iconArray.put(70, R.mipmap.app_six_mark_icon)
        iconArray.put(85, R.mipmap.app_haste_six_mark_icon)
        iconArray.put(80, R.mipmap.app_haste_car_icon)
        iconArray.put(81, R.mipmap.app_haster_flybrow_icon)
        iconArray.put(82, R.mipmap.app_min_one_icon)
        iconArray.put(83, R.mipmap.app_min_three_icon)
        iconArray.put(84, R.mipmap.app_min_five_icon)
        iconArray.put(86, R.mipmap.app_eleven_check_five)
        iconArray.put(87, R.mipmap.app_rapidly_pc_eggs_icon)
        iconArray.put(88, R.mipmap.app_german_racing_icon)
        iconArray.put(89, R.mipmap.app_german_airship_icon)
        iconArray.put(90, R.mipmap.app_welfare)
        iconArray.put(91, R.mipmap.app_welfare_quick)
        iconArray.put(92, R.mipmap.app_pk10_mc_icon)
        iconArray.put(93, R.mipmap.app_flybrow_mc_icon)
        iconArray.put(94, R.mipmap.app_happy_often_icon)
        iconArray.put(95, R.mipmap.app_happy_shengxiao_icon)
        iconArray.put(96, R.mipmap.app_five_kthree_icon)
        return iconArray
    }
}