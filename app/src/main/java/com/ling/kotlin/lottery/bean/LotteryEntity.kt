package com.ling.kotlin.lottery.bean

import android.os.Parcel
import android.os.Parcelable

data class LotteryEntity(
    val blockTime: Int,
    val curPeriodNum: Long,
    val isHot: Int,
    val lotteryId: Int,
    val lotteryName: String,
    val menuDetails: List<MenuEntity>,
    val nextTime: String,
    val remainTime: Int,
    val sysTime: Int,
    var convertRemainTime: Long,
    var isFollow: Boolean
)

data class MenuEntity(
    val id: String,
    val isHot: Int,
    val lotteryId: Int,
    val menuDetails: List<MenuChildEntity>,
    val menuName: String,
    val sort: Int,
    var selectId: Int,
    var menuType: Int
) : Parcelable{
    constructor(source: Parcel) : this(
    source.readString(),
    source.readInt(),
    source.readInt(),
        //如果您放置一个空列表，当前生成的parcelables列表代码将崩溃。
        //解决Caused by: java.lang.IllegalStateException: source.createTypedArrayL…(实体) must not be null
        listOfNotNull<MenuChildEntity>().apply {
            source.readTypedList(this,MenuChildEntity.CREATOR)
        },
    source.readString(),
    source.readInt(),
    source.readInt(),
    source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeInt(isHot)
        writeInt(lotteryId)
        writeTypedList(menuDetails)
        writeString(menuName)
        writeInt(sort)
        writeInt(selectId)
        writeInt(menuType)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MenuEntity> = object : Parcelable.Creator<MenuEntity> {
            override fun createFromParcel(source: Parcel): MenuEntity = MenuEntity(source)
            override fun newArray(size: Int): Array<MenuEntity?> = arrayOfNulls(size)
        }
    }
}

class MenuChildEntity(val menuName: String, val lotteryId: Int, val id: String, var selectId: Int) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(menuName)
        writeInt(lotteryId)
        writeString(id)
        writeInt(selectId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MenuChildEntity> = object : Parcelable.Creator<MenuChildEntity> {
            override fun createFromParcel(source: Parcel): MenuChildEntity = MenuChildEntity(source)
            override fun newArray(size: Int): Array<MenuChildEntity?> = arrayOfNulls(size)
        }
    }
}

class DrawerEntity(val typeName:String, var type:Int, var data:List<LotteryEntity>)