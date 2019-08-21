package com.ling.kotlin.common

import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.retroft.BaseResponse
import com.ling.kotlin.retroft.HttpConfig
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.NetUtils
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.http.POST



interface ApiService {

    /**
     * 获取彩种列表
     */

    @POST("lottery/info/all/0")
    fun getLotteryList(@HeaderMap headerMap: Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<List<LotteryEntity>>>


    /**
     * 获取开奖历史
     */
    @FormUrlEncoded
    @POST("lottery/hisRes/{lotteryId}")
    fun getLotteryHistoryList(@Path("lotteryId")lotteryId:Int,@FieldMap map: Map<String, String>,@HeaderMap headerMap: Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<List<HistoryEntity>>>

    /**
     * 获取当前期号
     */
    @POST("lottery/info/{lotteryId}/{type}")
    fun getCurPeriodTime(@Path("lotteryId")lotteryId:Int,@Path("type")type:Int,@HeaderMap headerMap: Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<PeriodTimeEntity>>

    /**
     * 获取彩种投注内容
     */
    @GET("lottery/find/play/{lotteryId}/{menuId}")
    fun getLotteryInfo(@Path("lotteryId") lotteryId: Int,@Path("menuId")menuId:String,@HeaderMap headerMap: Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<List<LotteryGroupInfoEntity>>>

    /**
     * 获取回归活动
     */
    @GET("user/getReturnMoneyFlag")
    fun getActivity(@HeaderMap headerMap: Map<String, String> =HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>

    /**
     * 获取banner数据
     */
    @GET("adver/find/1")
    fun getBannerEntitys(@HeaderMap headerMap: Map<String,String> = HeaderMapUtils.commonHeader()): Observable<BaseResponse<BannerResponse>>

    /**
     * 获取公告数据
     */
    @FormUrlEncoded
    @POST("notice/find")
    fun getNoticeEntitys(@FieldMap partMap: Map<String,String>, @HeaderMap headerMap: Map<String, String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<List<NoticeEntity>>>

    /**
     * 获取用户钱包金额
     */
    @GET("api/user/userinfo/balance")
    fun getWalletBalance(@HeaderMap headerMap: Map<String, String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<WalletBalanceEntity>>

    /**
     * 投注请求
     */
    @FormUrlEncoded
    @POST("order/bet/{lotteryId}")
    fun betting(@Path("lotteryId")lotteryId:Int,@FieldMap partMap: Map<String,String>,@HeaderMap headerMap: Map<String, String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>

    /**
     * 未结注单请求
     */
    @FormUrlEncoded
    @POST("order/find")
    fun openNoteEntity(@FieldMap partMap: Map<String, String>,@HeaderMap headerMap: Map<String, String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<OpenNoteEntity>>

    /**
     * 撤单请求
     */
    @GET("order/cancelBet/{id}")
    fun delOpenNoteEntity(@Path("id")id:String,@HeaderMap headerMap: Map<String, String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>
    /**
     * 棋牌余额全部提取接口
     */
    @GET("chessCard/withdrawAll")
    fun getChessWithdrawAllUrl(@HeaderMap headerMap:Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>

    @POST("common/getValidImage")
    fun getVerifyCode(@HeaderMap headerMap:Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>

}


class HeaderMapUtils{
    companion object{
        fun commonHeader():Map<String,String> = mapOf(
            "Staffid" to HttpConfig.KEY_MAP,
            "Timestamp" to (System.currentTimeMillis()/1000).toString(),
            "username" to "dsnao112",
            "token" to "70e67edae1cb4aa48c44c5e366bd4059")
    }
}
