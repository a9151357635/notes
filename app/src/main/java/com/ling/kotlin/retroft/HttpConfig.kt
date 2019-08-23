package com.ling.kotlin.retroft

/**
 * 作者：leavesC
 * 时间：2019/5/31 10:49
 * 描述：
 */
class HttpConfig {

    companion object {

        const val BASE_URL_OLD_MAP = "https://ddapi.interfacea.com/"
       const val BASE_URL_MAP = "http://192.168.0.15:9977/"

        const val HTTP_REQUEST_TYPE_KEY = "requestType"

        const val KEY = "Staffid"

        const val KEY_MAP = "08F22DF8F31643C78ADB8DC135E6DC92"
        const val CLIENT_TYPE = "Android"
        const val CONTENT_TYPE = "application/json"
        //        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
        const val HTTP_UNAUTHORIZED = 401 //未授权
        const val HTTP_FORBIDDEN = 403 //没权限
        const val HTTP_PARAMTER = 400 //参数错误
        const val HTTP_NOT_FOUND = 404 //没找到资源
        const val HTTP_REQUEST_TIMEOUT = 408 //请求
        const val HTTP_INTERNAL_SERVER_ERROR = 500 //服务器内部错误
        const val HTTP_BAD_GATEWAY = 502 //网关错误
        const val HTTP_SERVICE_UNAVAILABLE = 503 //暂停服务
        const val HTTP_GATEWAY_TIMEOUT = 504 //网关超时
        /**
         * 未知错误
         */
        const val CODE_UNKNOWN = -1024

        /**
         * 请求成功
         */
        const val CODE_SUCCESS = 200

        /**
         * token失效
         */
        const val CODE_TOKEN_INVALID = 430

        /**
         * 参数错误
         */
        const val CODE_PARAMTER_INVALID = 400
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002
        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003
        /**
         * 服务器连接超时
         */
        const val HTTP_CONNECT_TIMEOUT = 1004

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

    }

}