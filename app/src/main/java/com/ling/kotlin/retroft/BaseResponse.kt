package com.ling.kotlin.retroft

import com.google.gson.annotations.SerializedName

class BaseResponse<T>(@SerializedName("code")var code:Int,
                      @SerializedName("msg") var msg:String?= null,
                      @SerializedName("data",alternate = ["forecasts"])var data:T)

class OptionT<T>(val  value:T)