package com.ling.kotlin.utils

import com.ling.kotlin.R
import java.util.HashMap

object  BankUtils {
    private  var bankMap: MutableMap<String, Int> = HashMap()
    /**
     * 各个银行的图标
     */
    fun getBankMapIcon(): Map<String, Int> {
        if(bankMap.isNotEmpty()){
            return bankMap
        }
        bankMap["ICBC"] = R.drawable.ic_bank_icbc//工商银行
        bankMap["CMB"] = R.drawable.ic_bank_cmb  //招商银行
        bankMap["CCB"] = R.drawable.ic_bank_ccb //建设银行
        bankMap["ABC"] = R.drawable.ic_bank_abc//农业银行
        bankMap["BOC"] = R.drawable.ic_bank_boc  //中国银行
        bankMap["CMBC"] = R.drawable.ic_bank_cmbc//民生银行
        bankMap["CEB"] = R.drawable.ic_bank_ceb//光大银行
        bankMap["BCM"] = R.drawable.ic_bank_bcm //交通银行
        bankMap["SPDB"] = R.drawable.ic_bank_spdb //浦发银行
        bankMap["PAB"] = R.drawable.ic_bank_pab //平安银行
        bankMap["CIB"] = R.drawable.ic_bank_cib//兴业银行
        bankMap["CNCB"] = R.drawable.ic_bank_zxb//中信银行
        bankMap["ZXB"] = R.drawable.ic_bank_zxb//中信银行
        bankMap["GDB"] = R.drawable.ic_bank_gdb//广发银行
        bankMap["PSBC"] = R.drawable.ic_bank_psbc//邮政银行
        bankMap["HXB"] = R.drawable.ic_bank_hxb //华夏银行
        bankMap["RCB"] = R.drawable.ic_bank_rcb//浙江农商
        bankMap["CDYH"] = R.drawable.ic_bank_cdyh//成都银行
        bankMap["CZB"] = R.drawable.ic_bank_czb//浙商银行
        bankMap["ZJNCXYS"] = R.drawable.ic_bank_czb//浙商银行
        bankMap["BOJ"] = R.drawable.ic_bank_boj//锦州银行
        bankMap["SJB"] = R.drawable.ic_bank_sjb//盛京银行
        bankMap["LCC"] = R.drawable.ic_bank_lcc//辽宁农村信用社
        bankMap["LTD"] = R.drawable.ic_bank_ltd//吉林银行
        bankMap["BOD"] = R.drawable.ic_bank_bod//大连银行
        bankMap["DLB"] = R.drawable.ic_bank_bod//大连银行
        bankMap["BOH"] = R.drawable.ic_bank_boh//葫芦岛银行
        bankMap["HBB"] = R.drawable.ic_bank_hbb//哈尔滨银行
        bankMap["BRCB"] = R.drawable.ic_bank_brcb//北京农村商业银行
        bankMap["SRCB"] = R.drawable.ic_bank_srcb//上海农商银行
        bankMap["ZJKYH"] = R.drawable.ic_bank_zjkyh//张家口银行
        bankMap["CQYH"] = R.drawable.ic_bank_cqyh//重庆银行
        bankMap["CQNCSYYH"] = R.drawable.ic_bank_cqncsyyh//重庆农村商业银行
        bankMap["XAYH"] = R.drawable.ic_bank_xayh//西安银行
        bankMap["LZYH"] = R.drawable.ic_bank_lzyh//兰州银行
        bankMap["LFYH"] = R.drawable.ic_bank_lfyh//廊坊银行
        bankMap["FJNS"] = R.drawable.ic_bank_fjns//福建农商银行
        bankMap["CDNS"] = R.drawable.ic_bank_cdns//成都农商银行
        bankMap["RZYH"] = R.drawable.ic_bank_rzyh//日照银行
        bankMap["QSYH"] = R.drawable.ic_bank_qsyh//齐商银行
        bankMap["LSYH"] = R.drawable.ic_bank_lsyh//临商银行
        bankMap["GLYH"] = R.drawable.ic_bank_glyh//桂林银行
        bankMap["BJYH"] = R.drawable.ic_bank_bob//北京银行
        bankMap["BOB"] = R.drawable.ic_bank_bob//北京银行
        bankMap["TJYH"] = R.drawable.ic_bank_tjyh//天津银行
        bankMap["HBYH"] = R.drawable.ic_bank_hbyh//河北银行
        bankMap["JSYH"] = R.drawable.ic_bank_jsyh//江苏银行
        bankMap["JXYH"] = R.drawable.ic_bank_jxyh//江西银行
        bankMap["GZYH"] = R.drawable.ic_bank_gzyh//赣州银行
        bankMap["HYB"] = R.drawable.ic_bank_hyb //华一银行
        bankMap["RCC"] = R.drawable.ic_bank_rcc //农村信用社
        bankMap["SHB"] = R.drawable.ic_bank_shb // 上海银行
        bankMap["SDB"] = R.drawable.ic_bank_sdb //深圳发展银行
        bankMap["YSF"] = R.drawable.ic_bank_ysf//云闪付
        return bankMap
    }
}
