package com.ling.kotlin.utils

import com.ling.kotlin.R
import java.util.HashMap

object  BankUtils {
    private lateinit var bankMap: MutableMap<String, Int>
    /**
     * 各个银行的图标
     */
    fun getBankMapIcon(): Map<String, Int> {
        if (bankMap == null) {
            bankMap = HashMap()
        }
        bankMap["ICBC"] = R.drawable.ic_bank_gongshang//工商银行
        bankMap["CMB"] = R.drawable.ic_bank_zhaoshang  //招商银行
        bankMap["CCB"] = R.drawable.ic_bank_jianshe //建设银行
        bankMap["ABC"] = R.drawable.ic_bank_nongye//农业银行
        bankMap["BOC"] = R.drawable.ic_bank_china  //中国银行
        bankMap["CMBC"] = R.drawable.ic_bank_minsheng//民生银行
        bankMap["CEB"] = R.drawable.ic_bank_guangda//光大银行
        bankMap["BCM"] = R.drawable.ic_bank_jiaotong //交通银行
        bankMap["SPDB"] = R.drawable.ic_bank_pufa //浦发银行
        bankMap["PAB"] = R.drawable.ic_bank_pingan //平安银行
        bankMap["CIB"] = R.drawable.ic_bank_xingye//兴业银行
        bankMap["CNCB"] = R.drawable.ic_bank_zhongxin//中信银行
        bankMap["GDB"] = R.drawable.ic_bank_guangfa//广发银行
        bankMap["PSBC"] = R.drawable.ic_bank_youzheng//邮政银行
        bankMap["HXB"] = R.drawable.ic_bank_huaxia //华夏银行
        bankMap["RCB"] = R.drawable.ic_bank_zhejiang_xinyong//浙江农商
        bankMap["CDYH"] = R.drawable.ic_bank_chengdu//成都银行
        bankMap["CZB"] = R.drawable.ic_bank_zheshang//浙商银行
        bankMap["ZJNCXYS"] = R.drawable.ic_bank_zheshang//浙商银行
        bankMap["BOJ"] = R.drawable.ic_bank_jingzhou//锦州银行
        bankMap["SJB"] = R.drawable.ic_bank_shengjing//盛京银行
        bankMap["LCC"] = R.drawable.ic_bank_liaoning_xinyong//辽宁农村信用社
        bankMap["LTD"] = R.drawable.ic_bank_jilin//吉林银行
        bankMap["BOD"] = R.drawable.ic_bank_dalian//大连银行
        bankMap["BOH"] = R.drawable.ic_bank_huludao//葫芦岛银行
        bankMap["HBB"] = R.drawable.ic_bank_haerbing//哈尔滨银行
        bankMap["BRCB"] = R.drawable.ic_bank_beijing_nongshang//北京农村商业银行
        bankMap["SRCB"] = R.drawable.ic_bank_shanghai_nongshang//上海农商银行
        bankMap["ZJKYH"] = R.drawable.ic_bank_zhangjiakou//张家口银行
        bankMap["CQYH"] = R.drawable.ic_bank_chongqing//重庆银行
        bankMap["CQNCSYYH"] = R.drawable.ic_bank_chongqing_nongshang//重庆农村商业银行
        bankMap["XAYH"] = R.drawable.ic_bank_xian//西安银行
        bankMap["LZYH"] = R.drawable.ic_bank_lanzhou//兰州银行
        bankMap["LFYH"] = R.drawable.ic_bank_langfang//廊坊银行
        bankMap["FJNS"] = R.drawable.ic_bank_fujian_nongshang//福建农商银行
        bankMap["CDNS"] = R.drawable.ic_bank_chengdu_nongshang//成都农商银行
        bankMap["RZYH"] = R.drawable.ic_bank_rizhao//日照银行
        bankMap["QSYH"] = R.drawable.ic_bank_qishang//齐商银行
        bankMap["LSYH"] = R.drawable.ic_bank_linshang//临商银行
        bankMap["GLYH"] = R.drawable.ic_bank_guilin//桂林银行
        bankMap["BJYH"] = R.drawable.ic_bank_beijing//北京银行
        bankMap["TJYH"] = R.drawable.ic_bank_tianjing//天津银行
        bankMap["HBYH"] = R.drawable.ic_bank_hebei//河北银行
        bankMap["JSYH"] = R.drawable.ic_bank_jiangsu//江苏银行
        bankMap["JXYH"] = R.drawable.ic_bank_jiangxi//江西银行
        bankMap["GZYH"] = R.drawable.ic_bank_ganzhou//赣州银行
        bankMap["YSF"] = R.drawable.ic_bank_yunshanfu//云闪付
        return bankMap
    }
}
