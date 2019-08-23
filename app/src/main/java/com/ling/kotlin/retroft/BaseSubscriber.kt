package com.ling.kotlin.retroft

import io.reactivex.observers.DisposableObserver

import com.google.gson.JsonParseException
import com.ling.kotlin.utils.ToastUtils
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * 基类通知，网络请求的基本处理信息
 */
class BaseSubscriber<T> constructor(private val requestCallback: RequestCallback<T>) :DisposableObserver<OptionT<T>>(){

    override fun onComplete() {

    }

    override fun onNext(t: OptionT<T>) {
        requestCallback.onSuccess(t.value)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        var msg:String
        var code:Int
        when (e) {
            is TokenInvalidResultException ->{
                msg = e.message ?: "登录失效"
                code = e.code
            }
            is BaseException ->{
                msg = e.message ?: "未知错误"
                code = e.code
            }
            is HttpException -> {
                msg =  when(e.code()){
                    HttpConfig.HTTP_UNAUTHORIZED -> "未授权"
                    HttpConfig.HTTP_FORBIDDEN -> "没权限"
                    HttpConfig.HTTP_PARAMTER -> "参数错误"
                    HttpConfig.HTTP_NOT_FOUND -> "没找到资源"
                    HttpConfig.HTTP_REQUEST_TIMEOUT -> "请求超时"
                    HttpConfig.HTTP_INTERNAL_SERVER_ERROR -> "服务器内部错误"
                    HttpConfig.HTTP_BAD_GATEWAY ->"网关错误"
                    HttpConfig.HTTP_SERVICE_UNAVAILABLE -> "暂停服务"
                    HttpConfig.HTTP_GATEWAY_TIMEOUT -> "网关超时"
                    else -> "未知错误"
                }
                code = e.code()
            }
            is JsonParseException, is JSONException ->{
                msg = "解析失败"
                code = HttpConfig.PARSE_ERROR
            }
            is ConnectException ->{
                msg ="服务器连接失败"
                code = HttpConfig.NETWORD_ERROR
            }
            is SocketTimeoutException -> {
                msg ="服务器连接超时"
                code =HttpConfig.HTTP_CONNECT_TIMEOUT
            }
            is SSLHandshakeException -> {
                msg = "证书验证失败"
                code =HttpConfig.SSL_ERROR
            }
            else -> {
                msg = e.message ?: "未知错误"
                code = HttpConfig.CODE_UNKNOWN
            }
        }
        if(requestCallback is RequestMultiplayCallback){
            requestCallback.onFail(ServerResultException(msg,code))
        }else{
            ToastUtils.showToast(msg = "msg :$msg,code = $code")
        }
    }

}