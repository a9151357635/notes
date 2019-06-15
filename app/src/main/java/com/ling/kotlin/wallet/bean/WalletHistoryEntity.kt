package com.ling.kotlin.wallet.bean

import android.os.Parcel
import android.os.Parcelable

data class WalletHistoryEntity(
    val createdTime: String,
    val description: String,
    val money: Float,
    val moneyStr: String,
    val type: String,
    val typeId: Int
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readFloat(),
        source.readString(),
        source.readString(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(createdTime)
        writeString(description)
        writeFloat(money)
        writeString(moneyStr)
        writeString(type)
        writeInt(typeId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WalletHistoryEntity> = object : Parcelable.Creator<WalletHistoryEntity> {
            override fun createFromParcel(source: Parcel): WalletHistoryEntity = WalletHistoryEntity(source)
            override fun newArray(size: Int): Array<WalletHistoryEntity?> = arrayOfNulls(size)
        }
    }
}