package com.ling.kotlin.lottery.utils

/**
 * 此文件实现数据共享，并且实现各个activity和fragment的解耦
 * 不需要关心activity/fragment的生命周期
 * 实际是一个观察者对象，需要的观察者添加观察监听即可拿到最新的数据
 */
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.common.HelperEntity
import com.ling.kotlin.common.WalletBalanceEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.bean.LotteryInfoEntity

/**
 * 实现数据共享(彩种集合(饿汉单例模式))
 */
object LotteryEntityLiveData : MutableLiveData<List<LotteryEntity>>()

/**
 * 数据清除
 */
object ClearLiveData : MutableLiveData<Boolean>()

/**
 *  筹码数据设置
 */
object ChipLiveData : MutableLiveData<Boolean>()

/**
 *  选中的组合数(饿汉单例模式)
 */
object CombineEntityLiveData : MutableLiveData<List<LotteryInfoEntity>>()

/**
 *  当前期号
 */
object CurPeriodNumLiveData : MutableLiveData<Long>()

/**
 * 是否可以投注
 */
object BetCanLiveData : MutableLiveData<Boolean>()
/**
 * 投注确认
 */
object BetLiveData: MutableLiveData<Double>()
/**
 * 用户余额(需要贯穿整个应用)
 */
object WalletBalanceLiveData:MutableLiveData<WalletBalanceEntity>()

/**
 * 投注帮助(重力感应，声音，黑夜白天)
 */
object HelperLiveData:MutableLiveData<HelperEntity>()

/**
 * 声音开启
 */
object OpenSoundLiveData: MutableLiveData<Boolean>()

/**
 * 传感器摇晃之后选中的随机数
 */
object InductionLiveData: MutableLiveData<Int>()

/**
 * 是否开启传感器摇晃
 */
object IsInductionLiveData:MutableLiveData<Boolean>()