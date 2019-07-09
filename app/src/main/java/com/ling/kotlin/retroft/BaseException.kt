package com.ling.kotlin.retroft

import java.lang.RuntimeException

/**
 * sealed封密关键字
 */
sealed class BaseException(errrorMsg:String, val code:Int = HttpConfig.CODE_UNKNOWN) :RuntimeException(errrorMsg)

/**
 * 错误信息统一处理
 */
class ServerResultException(message:String,code: Int = HttpConfig.CODE_UNKNOWN):
    BaseException(message,code)

/**
 * token失效
 */
class TokenInvalidResultException(message:String,code: Int = HttpConfig.CODE_TOKEN_INVALID) :
    BaseException(message,code)

/**
 * 参数错误
 */
class ParamterInvalidResultException(message:String,code: Int = HttpConfig.CODE_PARAMTER_INVALID) :
    BaseException(message,code)
