package com.ling.kotlin.retroft


class BaseResponse<T>(var code:Int,var msg:String?= null,var data:T)

class OptionT<T>(val  value:T)