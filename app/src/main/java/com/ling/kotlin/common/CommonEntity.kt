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