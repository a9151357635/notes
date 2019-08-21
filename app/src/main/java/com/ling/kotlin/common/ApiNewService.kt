package com.ling.kotlin.common

import com.ling.kotlin.login.entity.LoginEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.me.entity.UserInfoEntity
import com.ling.kotlin.retroft.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiNewService {
    /**
     * 注册
     */
    @POST("top-service-user/frontdesk/userLoginApi/register")
    fun register(@Body body: RequestBody): Observable<BaseResponse<LoginEntity>>

    /**
     * 登陆
     */
    @POST("top-service-user/frontdesk/userLoginApi/login")
    fun login(@Body body: RequestBody): Observable<BaseResponse<LoginEntity>>

    /**
     * 查询用户信息接口
     */
    @POST("top-service-user/frontdesk/personalCenter/getClientBaseInfo")
    fun findUserInfoEntity():Observable<BaseResponse<UserInfoEntity>>

    @GET("top-service-system/frontdesk/lottery/initPeriod")
    fun findLotteryEntitys():Observable<BaseResponse<List<LotteryEntity>>>
}

class HeaderUtils {
    companion object {
        fun commonNewHeader(): Map<String, String> = mapOf(
            "sign" to ""
        )  //签名(针对参数做的加密)
    }
}