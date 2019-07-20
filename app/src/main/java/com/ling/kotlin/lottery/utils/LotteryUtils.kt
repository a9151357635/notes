package com.ling.kotlin.lottery.utils

import android.util.SparseIntArray
import androidx.annotation.NonNull
import com.ling.kotlin.R
import java.util.*

object LotteryUtils {
    val pkTenSeriesIdList = listOf(50, 55, 80, 81, 88, 89, 92, 93)
    val kThreesSeriesIdList = listOf(10, 11, 12, 13, 14, 15, 96)
    //时时彩系列lotteryId集合
    val constantlySeriesIdList = listOf(1, 4, 5, 82, 83, 84, 94, 95)
    //PC蛋蛋系列lotteryId集合
    val pcAgeSeriesIdList = listOf(66, 87)
    //六合彩
    val sixMarkIdList = listOf(70, 85)
    //福彩3D系列
    val welfareSeriesIdList = listOf(90, 91)
    //11选五系列
    val elevenIdList = listOf(86)

    fun getAppIcon(): SparseIntArray {
        val iconArray = SparseIntArray()
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
    //数字背景map
    val numberMap = mapOf(
        1 to R.drawable.ic_number_one,
        2 to R.drawable.ic_number_two,
        3 to R.drawable.ic_number_three,
        4 to R.drawable.ic_number_four,
        5 to R.drawable.ic_number_five,
        6 to R.drawable.ic_number_six,
        7 to R.drawable.ic_number_seven,
        8 to R.drawable.ic_number_eight,
        9 to R.drawable.ic_number_nine,
        10 to R.drawable.ic_number_ten
    )
    //欢乐生肖背景map
    val ballMap = mapOf(
        0 to R.mipmap.ball_cqhlsx_zero,
        1 to R.mipmap.ball_cqhlsx_one,
        2 to R.mipmap.ball_cqhlsx_two,
        3 to R.mipmap.ball_cqhlsx_three,
        4 to R.mipmap.ball_cqhlsx_four,
        5 to R.mipmap.ball_cqhlsx_five,
        6 to R.mipmap.ball_cqhlsx_six,
        7 to R.mipmap.ball_cqhlsx_seven,
        8 to R.mipmap.ball_cqhlsx_eight,
        9 to R.mipmap.ball_cqhlsx_nine
    )
    //骰子背景map
    val dicemap = mapOf(
        1 to R.drawable.ic_dice_one,
        2 to R.drawable.ic_dice_two,
        3 to R.drawable.ic_dice_three,
        4 to R.drawable.ic_dice_four,
        5 to R.drawable.ic_dice_five,
        6 to R.drawable.ic_dice_six
    )
    //颜色背景map
    val colorMap = mapOf(
        "0" to R.drawable.ic_circle_red,
        "1" to R.drawable.ic_circle_green,
        "2" to R.drawable.ic_circle_blue
    )
    /**
     * 根据开奖号码计算出来的其他开奖结果(比如龙虎和等)
     * @param openNumbers
     * @return
     */
    fun openNumberConvertContent(lotteryId: Int, openNumbers: List<String>?): List<String>? {
        if (openNumbers.isNullOrEmpty()) return listOf()
        val size = openNumbers.size
        if (lotteryId in pkTenSeriesIdList && size == 10) return dealSpiritList(
            openNumbers
        )      //PK10系列
        else if (lotteryId in constantlySeriesIdList && size == 5) return dealConstantly(
            openNumbers
        ) //时时彩系列
        else if ((lotteryId in kThreesSeriesIdList || lotteryId in pcAgeSeriesIdList || lotteryId in welfareSeriesIdList) && size == 3) return dealKThreeList(
            openNumbers,
            lotteryId
        )  //快三系列和PC蛋蛋(66)
        else if (86 == lotteryId && size == 5) return dealElevenCheckFive(
            openNumbers
        ) //11选五系列
        return listOf()
    }

    /**
     * 处理显示龙虎和，主要的彩种是北京PK10系列彩种
     * @param openNumbers
     * @return
     */
    @NonNull
    private fun dealSpiritList(openNumbers: List<String>): List<String> {
        val spiritList = ArrayList<String>()
        val startNumber = Integer.valueOf(openNumbers[0].trim { it <= ' ' })
        val endNumber = Integer.valueOf(openNumbers[1].trim { it <= ' ' })
        val total = startNumber + endNumber
        spiritList.add(total.toString())
        spiritList.add(if (total > 11) "大" else "小")
        spiritList.add(if (total % 2 == 0) "双" else "单")
        var end = openNumbers.size - 1
        openNumbers.forEachIndexed { index, _ ->
            if (index < 5) {
                spiritList.add(spiritOrEssence(index, end, openNumbers))
                end--
            }
        }
        return spiritList
    }

    /**
     * 龙虎
     * @param start
     * @param end
     * @param strings
     * @return
     */
    private fun spiritOrEssence(start: Int, end: Int, strings: List<String>): String {
        val startNumber = Integer.valueOf(strings[start].trim { it <= ' ' })
        val endNumber = Integer.valueOf(strings[end].trim { it <= ' ' })
        return if (startNumber - endNumber > 0) "龙" else "虎"
    }


    /**
     * 处理时时彩系列的其他显示
     * @param openNumbers
     * @return
     */
    private fun dealConstantly(openNumbers: List<String>): List<String> {
        val totalList = ArrayList<String>()
        val total = calculateTotal(openNumbers)
        totalList.add(total.toString())
        totalList.add(if (total >= 23) "大" else "小")
        totalList.add(if (total % 2 == 0) "双" else "单")
        //前集合
        totalList.add(
            calculateList(
                transformNewList(
                    openNumbers,
                    0
                )
            )
        )
        //中集合
        totalList.add(
            calculateList(
                transformNewList(
                    openNumbers,
                    1
                )
            )
        )
        //后集合
        totalList.add(
            calculateList(
                transformNewList(
                    openNumbers,
                    2
                )
            )
        )
        return totalList
    }

    /**
     * 算出号码总和
     * @param openNumber
     * @return
     */
    private fun calculateTotal(openNumber: List<String>): Int {
        var total = 0
        for (number in openNumber) {
            val temp = Integer.valueOf(number)
            total += temp
        }
        return total
    }

    /**
     * 根据mode来抓换新的数据集合
     * @param openNumbers
     * @param mode
     * @return
     */
    private fun transformNewList(openNumbers: List<String>, mode: Int): List<String> {
        val newList = ArrayList(openNumbers)
        var size = newList.size
        when (mode) {
            0 -> {
                newList.removeAt(size - 1)
                newList.removeAt(size - 2)
            }
            1 -> {
                newList.removeAt(0)
                size = newList.size
                newList.removeAt(size - 1)
            }
            2 -> {
                newList.removeAt(0)
                newList.removeAt(0)
            }
        }
        newList.sort()
        return newList
    }

    /***
     * 计算新集合得到的相关数据
     * @param list
     * @return
     */
    private fun calculateList(list: List<String>): String {
        val one = Integer.valueOf(list[0])
        val two = Integer.valueOf(list[1])
        val three = Integer.valueOf(list[2])
        val tempTwo = one + 1
        val tempThree = one + 2
        val tempFour = two + 1
        if (one == two && two == three) return "豹子"
        else if (tempTwo == two && tempThree == three || one == 0 && two == 8 && three == 9 || one == 0 && two == 1 && three == 9) return "顺子" //特殊情况one== 0 && two == 8 && three == 9 || one == 0 && two == 1 && three == 9
        else if (one == two || two == three) return "对子"
        else if (tempTwo == two || tempThree == three || tempFour == three || one == 0 && three == 9) return "半顺"    //特殊情况( one== 0  && three == 9)
        return "杂六"
    }

    /**
     * 快三系列，3个开奖号码
     * @param openNumbers
     * @return
     */
    private fun dealKThreeList(openNumbers: List<String>, lotteryId: Int): List<String> {
        val totalList = ArrayList<String>()
        val total = calculateTotal(openNumbers)
        totalList.add(total.toString())
        if (lotteryId in pcAgeSeriesIdList || lotteryId in welfareSeriesIdList) {
            totalList.add(if (total > 13) "大" else "小")
        } else {
            totalList.add(if (total >= 11) "大" else "小")
        }
        totalList.add(if (total % 2 == 0) "双" else "单")
        return totalList
    }

    /**
     * 广东11选5的开奖结果处理
     * @param openNumbers
     * @return
     */
    private fun dealElevenCheckFive(openNumbers: List<String>): List<String> {
        val totalList = ArrayList<String>()
        val total = calculateTotal(openNumbers)
        totalList.add(total.toString())
        totalList.add(if (total <= 30) "小 " else "大")
        totalList.add(if (total % 2 == 0) "双" else "单")
        //尾数大小
        totalList.add(if (total % 10 <= 4) "小" else "大")
        totalList.add(spiritOrEssence(0, openNumbers.size - 1, openNumbers))
        return totalList
    }

}

object LotteryId {
    //最少一注的集合
    val leastOneList: List<String> = listOf(
        MarkSix.NO_CHOICE_PLAYID,//香港自选不中
        MarkSix.LUCKY_NO_CHOICE_PLAYID,//幸运六合彩自选不中
        ElevenPlayId.SECOND_TWO_PLAYID,//广东11选5的连码的二中二
        ElevenPlayId.THREE_MIDDLE_THREE_PLAYID,//广东11选5的连码的三中三
        ElevenPlayId.FOUR_FOUR_PLAYID,//广东11选5的连码的4中4
        ElevenPlayId.FIVE_FIVE_PLAYID,//广东11选5的连码的五中五
        ElevenPlayId.SIX_FIVE_PLAYID,//广东11选5的连码的六中五
        ElevenPlayId.SEVEN_TO_FIVE_PLAYID,//广东11选5的连码的七中五
        ElevenPlayId.EIGHT_FIVE_PLAYID,//广东11选5的连码的八中五
        WelfareId.WELFARE_GROUPTHREE_PLAYID,//福彩3D组选3
        WelfareId.WELFARE_GROUPSIX_PLAYID, // 福彩3D组选6
        WelfareId.WELFARE_QUICK_GROUPTHREE_PLAYID,//急速福彩3D组选3
        WelfareId.WELFARE_QUICK_GROUPSIX_PLAYID // 急速福彩3D组选6
    )
    //最少选俩注集合
    val leastTwoList: List<String> = listOf(
        MarkSix.TWO_FULL_PLAYID,  // 香港六合彩二全中
        MarkSix.SECOND_SPECIAL_PLAYID, // 香港六合彩二中特
        MarkSix.SPECIAL_STRING_PLAYID, // 香港六合彩特串
        MarkSix.LUCKY_TWO_FULL_PLAYID, //幸运六合彩二全中
        MarkSix.LUCKY_SECOND_SPECIAL_PLAYID, //幸运六合彩二中特
        MarkSix.LUCKY_SPECIAL_STRING_PLAYID,//幸运六合彩特串
        ElevenPlayId.TOP_TWO_GROUP_PLAYID, //广东11选5的连码的前二组选
        ElevenPlayId.STRAIGHT_TWO_PLAYID,//广东11选5直选的前二直选
        WelfareId.WELFARE_TWO_LOCATION_PLAYID,//福彩3D二字定位 百拾定位
        WelfareId.WELFARE_TWO_FIRST_LOCATION_PLAYID,//福彩3D二字定位 百个定位
        WelfareId.WELFARE_TWO_SECOND_LOCATION_PLAYID,//福彩3二字定位 拾个定位
        WelfareId.WELFARE_QUICK_TWO_LOCATION_PLAYID,//福彩急速二字定位 百拾定位
        WelfareId.WELFARE_QUICK_TWO_FIRST_LOCATION_PLAYID,//福彩急速二字定位 百个定位
        WelfareId.WELFARE_QUICK_TWO_SECOND_LOCATION_PLAYID,//福彩急速二字定位 拾个定位
        WelfareId.WELFARE_THREE_LOCATION_PLAYID,//福彩3D二字定位
        WelfareId.WELFARE_QUICK_THREE_LOCATION_PLAYID,//福彩3D二字定位
        MarkSix.TWO_EVEN_XIAO_MENUID,//香港六合彩二连肖
        MarkSix.TWO_EVEN_TAIL_MENUID, //香港六合彩二连尾
        MarkSix.LUCKY_TWO_EVEN_XIAO_MENUID,//幸运六合彩二连肖
        MarkSix.LUCKY_TWO_EVEN_TAIL_MENUID//幸运六合彩二连尾
    )

    //最少选3注集合
    val leastThreeList = listOf(
        MarkSix.THREE_MIDDLE_TWO_PLAYID, //香港六合彩三全中
        MarkSix.THREE_FULL_PLAYID, //香港六合彩三中二
        MarkSix.LUCKY_THREE_MIDDLE_TWO_PLAYID, //幸运六合彩三中二
        MarkSix.LUCKY_THREE_FULL_PLAYID,//幸运六合彩三全中
        ElevenPlayId.TOP_THREE_GROUP_PLAYID, //广东11选5的连码的前三组选
        ElevenPlayId.STRAIGHT_THREE_PLAYID,//广东11选5直选的前三直选
        MarkSix.THREE_EVEN_XIAO_MENUID,//香港六合彩三连肖
        MarkSix.THREE_EVEN_TAIL_MENUID,//香港六合彩三连尾
        MarkSix.LUCKY_THREE_EVEN_XIAO_MENUID,//幸运六合彩三连肖
        MarkSix.LUCKY_THREE_EVEN_TAIL_MENUID//幸运六合彩三连尾
    )
    //最少选4注集合
    val leastFourList: List<String> = listOf(
        MarkSix.FOUR_FULL_PLAYID,//香港六合彩四全中
        MarkSix.LUCKY_FOUR_FULL_PLAYID,//幸运六合彩四全中
        MarkSix.FOUR_EVEN_XIAO_MENUID,//香港六合彩四连肖
        MarkSix.FOUR_EVEN_TAIL_MENUID,//香港六合彩四连尾
        MarkSix.LUCKY_FOUR_EVEN_XIAO_MENUID,//幸运六合彩四连肖
        MarkSix.LUCKY_FOUR_EVEN_TAIL_MENUID//幸运六合彩四连尾
    )

    //最少选5注集合
    val leastFiveList = listOf(
        MarkSix.FIVE_EVEN_XIAO_MENUID,// 香港六合彩五连肖
        MarkSix.FIVE_EVEN_TAIL_MENUID,// 香港六合彩五连尾
        MarkSix.LUCKY_FIVE_EVEN_XIAO_MENUID,//幸运六合彩五连肖
        MarkSix.LUCKY_FIVE_EVEN_TAIL_MENUID//幸运六合彩五连尾
    )

    val parentMenuIdList = listOf(
        ParentMenuId.MARK_SIX_SERIAL_CODE_MENUID,
        ParentMenuId.MARK_SIX_NO_CHOICE_MENUID,
        ParentMenuId.MARK_SIX_RAPIDLY_SERIAL_CODE_MENUID,
        ParentMenuId.MARK_SIX_RAPIDLY_NO_CHOICE_MENUID,
        ParentMenuId.ELEVEN_CHECK_SERIAL_CODE_MENUID
    )
    //福彩3D和急速福彩3D(组选3和组选六)
    val welfarPlayIdList = listOf(
        WelfareId.WELFARE_GROUPTHREE_PLAYID,
        WelfareId.WELFARE_GROUPSIX_PLAYID,
        WelfareId.WELFARE_QUICK_GROUPTHREE_PLAYID,
        WelfareId.WELFARE_QUICK_GROUPSIX_PLAYID
    )

    //福彩3D和急速福彩3D(二字定位)
    val welfarTwoPlayIdList= listOf(
        WelfareId.WELFARE_TWO_LOCATION_PLAYID,//福彩3D二字定位 百拾定位
        WelfareId.WELFARE_TWO_FIRST_LOCATION_PLAYID,//福彩3D二字定位 百个定位
        WelfareId.WELFARE_TWO_SECOND_LOCATION_PLAYID,//福彩3二字定位 拾个定位
        WelfareId.WELFARE_QUICK_TWO_LOCATION_PLAYID,//福彩急速二字定位 百拾定位
        WelfareId.WELFARE_QUICK_TWO_FIRST_LOCATION_PLAYID,//福彩急速二字定位 百个定位
        WelfareId.WELFARE_QUICK_TWO_SECOND_LOCATION_PLAYID//福彩急速二字定位 拾个定位
    )


    //选择的最大值
    val maxSelectMap = mapOf(
        2 to listOf(ElevenPlayId.SECOND_TWO_PLAYID),
        3 to listOf(ElevenPlayId.THREE_MIDDLE_THREE_PLAYID),
        4 to listOf(
            MarkSix.FOUR_FULL_PLAYID,
            MarkSix.LUCKY_FOUR_FULL_PLAYID,
            ElevenPlayId.FOUR_FOUR_PLAYID
        ),
        5 to listOf(
            MarkSix.THREE_MIDDLE_TWO_PLAYID,
            MarkSix.TWO_FULL_PLAYID,
            MarkSix.SECOND_SPECIAL_PLAYID,
            MarkSix.SPECIAL_STRING_PLAYID,
            MarkSix.LUCKY_THREE_MIDDLE_TWO_PLAYID,
            MarkSix.LUCKY_TWO_FULL_PLAYID,
            MarkSix.LUCKY_SECOND_SPECIAL_PLAYID,
            MarkSix.LUCKY_SPECIAL_STRING_PLAYID,
            ElevenPlayId.FIVE_FIVE_PLAYID,
            ElevenPlayId.TOP_TWO_GROUP_PLAYID,
            ElevenPlayId.TOP_THREE_GROUP_PLAYID
        ),
        6 to listOf(ElevenPlayId.SIX_FIVE_PLAYID),
        7 to listOf(ElevenPlayId.SEVEN_TO_FIVE_PLAYID),
        8 to listOf(
            WelfareId.WELFARE_GROUPSIX_PLAYID,
            WelfareId.WELFARE_QUICK_GROUPSIX_PLAYID,
            ElevenPlayId.EIGHT_FIVE_PLAYID
        ),
        10 to listOf(
            MarkSix.THREE_FULL_PLAYID,
            MarkSix.LUCKY_THREE_FULL_PLAYID
        ),
        12 to listOf(
            MarkSix.NO_CHOICE_PLAYID,
            MarkSix.LUCKY_NO_CHOICE_PLAYID
        )
    )

    //自选不中
    val markSixNotInPlayId = listOf(
        MarkSix.NO_CHOICE_PLAYID,
        MarkSix.LUCKY_NO_CHOICE_PLAYID
    )
    //11选五直选系列
    val straightPlayId = listOf(
        ElevenPlayId.STRAIGHT_TWO_PLAYID,
        ElevenPlayId.STRAIGHT_THREE_PLAYID
    )
    //福彩3D系列组选三
    val welfareThreePlayId = listOf(
        WelfareId.WELFARE_GROUPTHREE_PLAYID,
        WelfareId.WELFARE_QUICK_GROUPTHREE_PLAYID
    )
    //福彩3D系列组选六
    val welfareSixPlayId = listOf(
        WelfareId.WELFARE_GROUPSIX_PLAYID,
        WelfareId.WELFARE_QUICK_GROUPSIX_PLAYID
    )

}

/**
 * 11选5系类的playId
 */
interface ElevenPlayId {
    companion object {
        /***
         * 二中二的playId
         */
        const val SECOND_TWO_PLAYID = "868625"
        /***
         * 三中三的playId
         */
        const val THREE_MIDDLE_THREE_PLAYID = "868626"
        /***
         * 四中四的playId
         */
        const val FOUR_FOUR_PLAYID = "868627"
        /***
         * 五中五的playId
         */
        const val FIVE_FIVE_PLAYID = "868628"
        /***
         * 六中五的playId
         */
        const val SIX_FIVE_PLAYID = "868629"
        /***
         * 七中五的playId
         */
        const val SEVEN_TO_FIVE_PLAYID = "868630"
        /***
         * 八中五的playId
         */
        const val EIGHT_FIVE_PLAYID = "868631"
        /***
         * 前二组选的playId
         */
        const val TOP_TWO_GROUP_PLAYID = "868632"
        /***
         * 前三组选的playId
         */
        const val TOP_THREE_GROUP_PLAYID = "868633"
        /***
         * 前二直选的playId
         */
        const val STRAIGHT_TWO_PLAYID = "868634"
        /***
         * 前三直选的playId
         */
        const val STRAIGHT_THREE_PLAYID = "868635"
    }

}

interface MarkSix {
    companion object {
        //香港六合彩的PlayId
        const val NO_CHOICE_PLAYID = "7004101"//自选不中
        const val FOUR_FULL_PLAYID = "7002606" //四全中
        const val THREE_FULL_PLAYID = "7002602"  //三全中
        const val TWO_FULL_PLAYID = "7002603"  //二全中
        const val THREE_MIDDLE_TWO_PLAYID = "7002601" //三中二
        const val SECOND_SPECIAL_PLAYID = "7002604" //二中特
        const val SPECIAL_STRING_PLAYID = "7002605"  //特串
        //幸运六合彩的PlayId
        const val LUCKY_NO_CHOICE_PLAYID = "8504101"//自选不中
        const val LUCKY_FOUR_FULL_PLAYID = "852606" //四全中
        const val LUCKY_THREE_FULL_PLAYID = "852602"  //三全中
        const val LUCKY_TWO_FULL_PLAYID = "852603"  //二全中
        const val LUCKY_THREE_MIDDLE_TWO_PLAYID = "8502601" //三中二
        const val LUCKY_SECOND_SPECIAL_PLAYID = "852604" //二中特
        const val LUCKY_SPECIAL_STRING_PLAYID = "852605"  //特串

        //香港六合彩的menuId
        const val TWO_EVEN_XIAO_MENUID = "2D452A8CC19A48B7930C7D77962AF1D5" //二连肖
        const val THREE_EVEN_XIAO_MENUID = "4A222CB1FD724E1AA8B33236BC370382"//三连肖
        const val FOUR_EVEN_XIAO_MENUID = "22C49B57EE73496182C84F9202955BE9" //四连肖
        const val FIVE_EVEN_XIAO_MENUID = "5F20C2321E01422E9D479CA826EAF97E"//五连肖
        const val TWO_EVEN_TAIL_MENUID = "ADC43DAED51549A58BB3845025096562" //二连尾
        const val THREE_EVEN_TAIL_MENUID = "4FE41C6257FE417BA575A8A9E4F90212"//三连尾
        const val FOUR_EVEN_TAIL_MENUID = "AB6C0AB48E69459286FA855AA822733F" //四连尾
        const val FIVE_EVEN_TAIL_MENUID = "9F137727EC3C47568E702ED8C8FD5F40"//五连尾

        //幸运六合彩的menuId
        const val LUCKY_TWO_EVEN_XIAO_MENUID = "857C486479DC445EB7F91851CD850040" //二连肖
        const val LUCKY_THREE_EVEN_XIAO_MENUID = "857C486479DC445EB7F91851CD850041"//三连肖
        const val LUCKY_FOUR_EVEN_XIAO_MENUID = "857C486479DC445EB7F91851CD850042" //四连肖
        const val LUCKY_FIVE_EVEN_XIAO_MENUID = "857C486479DC445EB7F91851CD850043"//五连肖
        const val LUCKY_TWO_EVEN_TAIL_MENUID = "857C486479DC445EB7F91851CD850044" //二连尾
        const val LUCKY_THREE_EVEN_TAIL_MENUID = "857C486479DC445EB7F91851CD850045"//三连尾
        const val LUCKY_FOUR_EVEN_TAIL_MENUID = "857C486479DC445EB7F91851CD850046" //四连尾
        const val LUCKY_FIVE_EVEN_TAIL_MENUID = "857C486479DC445EB7F91851CD850047"//五连尾
    }

}

/**
 * 福彩3D
 */
interface WelfareId {
    companion object {
        //组选三
        const val WELFARE_GROUPTHREE_PLAYID = "900497"
        //组选六
        const val WELFARE_GROUPSIX_PLAYID = "900498"
        //急速组选三
        const val WELFARE_QUICK_GROUPTHREE_PLAYID = "910497"
        //急速组选六
        const val WELFARE_QUICK_GROUPSIX_PLAYID = "910498"
        //二字定位 百拾定位
        const val WELFARE_TWO_LOCATION_PLAYID = "900404"
        //二字定位 百个定位
        const val WELFARE_TWO_FIRST_LOCATION_PLAYID = "900405"
        //二字定位 拾个定位
        const val WELFARE_TWO_SECOND_LOCATION_PLAYID = "900406"
        //三字定位
        const val WELFARE_THREE_LOCATION_PLAYID = "900407"
        //急速二字定位 百拾定位
        const val WELFARE_QUICK_TWO_LOCATION_PLAYID = "910404"
        //急速二字定位 百个定位
        const val WELFARE_QUICK_TWO_FIRST_LOCATION_PLAYID = "910405"
        //急速二字定位 拾个定位
        const val WELFARE_QUICK_TWO_SECOND_LOCATION_PLAYID = "910406"
        //急速三字定位
        const val WELFARE_QUICK_THREE_LOCATION_PLAYID = "910407"
    }

}

/**
 * 父类Id集合，需要判断
 */
interface ParentMenuId {
    companion object {
        const val MARK_SIX_SERIAL_CODE_MENUID = "E0995214AB924FEA8B2177C3EE7EB518"//六合彩连码
        const val MARK_SIX_NO_CHOICE_MENUID = "987E450A0D20407BBB848021D454FD3B"//六合彩自选不中
        const val MARK_SIX_RAPIDLY_SERIAL_CODE_MENUID = "857C486479DC445EB7F91851CD850029"//急速六合彩连码
        const val MARK_SIX_RAPIDLY_NO_CHOICE_MENUID = "857C486479DC445EB7F91851CD850048"//急速六合彩自选不中
        const val ELEVEN_CHECK_SERIAL_CODE_MENUID = "867C486479DC445EB7F91851CD860008"//急速11选5连码的父类菜单id
    }
}