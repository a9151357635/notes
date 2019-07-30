package com.ling.kotlin.common

import android.os.Parcel
import android.os.Parcelable

/**
 * banner实体
 * Created by Ling on 2018/4/9.
 * imageUrl : http://images.ddcpcs.com/Advertisement/171212/baff7de4273940ae9e0c.png
 * jumpUrl : http://images.ddcpcs.com/Advertisement/171212/baff7de4273940ae9e0c.png
 */
data class BannerEntity(val imageUrl: String,val jumpUrl: String)


data class BannerResponse(val imageList:List<BannerEntity>)
/**
 * 公告实体
 */
data class NoticeEntity(
    val id: String,
    val title: String,
    val noticeContent: String,
    val createTime: String,
    val remark: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(noticeContent)
        writeString(createTime)
        writeString(remark)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NoticeEntity> = object : Parcelable.Creator<NoticeEntity> {
            override fun createFromParcel(source: Parcel): NoticeEntity = NoticeEntity(source)
            override fun newArray(size: Int): Array<NoticeEntity?> = arrayOfNulls(size)
        }
    }
}

/**
 * 钱包余额接口
 */
data class WalletBalanceEntity(
    val hLBalance: Double,
    val hLMonthlyWinMoney: Double,
    val hLTodayWinMoney: Double,
    val kYBalance: Double,
    val kYMonthlyWinMoney: Double,
    val kYTodayWinMoney: Double,
    val lotteryBalance: Double,
    val lotteryLockMoney: Double,
    val lotteryTodayWinMoney: Double,
    val todayWinMoney: Double,
    val totalBalance: Double
)

/**
 * 公用对话框实体对象
 */
data class CommonDialogEntity(
    var title: String?=null,
    var content: String?=null,
    var sureHint: String?=null,
    var cancelHint: String?=null,
    var inputHint: String?=null,
    var isShowCancel: Boolean = false,
    var isShowInput: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        1 == source.readInt(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(content)
        writeString(sureHint)
        writeString(cancelHint)
        writeString(inputHint)
        writeInt((if (isShowCancel) 1 else 0))
        writeInt((if (isShowInput) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CommonDialogEntity> = object : Parcelable.Creator<CommonDialogEntity> {
            override fun createFromParcel(source: Parcel): CommonDialogEntity = CommonDialogEntity(source)
            override fun newArray(size: Int): Array<CommonDialogEntity?> = arrayOfNulls(size)
        }
    }
}

/**
 * 帮助页面
 */
data class HelperEntity (var isSound:Boolean,var isInduction:Boolean,var isNight:Boolean)