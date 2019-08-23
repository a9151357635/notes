package com.ling.kotlin.common

import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.retroft.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*


interface ApiService {

    /**
     * 获取彩种列表
     */

    @POST("lottery/info/all/0")
    fun getLotteryList():Observable<BaseResponse<List<LotteryEntity>>>

    /**
     * 获取开奖历史
     */
    @FormUrlEncoded
    @POST("lottery/hisRes/{lotteryId}")
    fun getLotteryHistoryList(@Path("lotteryId")lotteryId:Int,@FieldMap map: Map<String, String>):Observable<BaseResponse<List<HistoryEntity>>>

    /**
     * 获取当前期号
     */
    @POST("lottery/info/{lotteryId}/{type}")
    fun getCurPeriodTime(@Path("lotteryId")lotteryId:Int,@Path("type")type:Int):Observable<BaseResponse<PeriodTimeEntity>>

    /**
     * 获取彩种投注内容
     */
    @GET("lottery/find/play/{lotteryId}/{menuId}")
    fun getLotteryInfo(@Path("lotteryId") lotteryId: Int,@Path("menuId")menuId:String):Observable<BaseResponse<List<LotteryGroupInfoEntity>>>

    /**
     * 获取回归活动
     */
    @GET("user/getReturnMoneyFlag")
    fun getActivity():Observable<BaseResponse<String>>

    /**
     * 获取banner数据
     */
    @GET("adver/find/1")
    fun getBannerEntitys(): Observable<BaseResponse<BannerResponse>>

    /**
     * 获取公告数据
     */
    @FormUrlEncoded
    @POST("notice/find")
    fun getNoticeEntitys():Observable<BaseResponse<List<NoticeEntity>>>

    /**
     * 获取用户钱包金额
     */
    @GET("api/user/userinfo/balance")
    fun getWalletBalance():Observable<BaseResponse<WalletBalanceEntity>>

    /**
     * 投注请求
     */
    @FormUrlEncoded
    @POST("order/bet/{lotteryId}")
    fun betting(@Path("lotteryId")lotteryId:Int,@FieldMap partMap: Map<String,String>):Observable<BaseResponse<String>>

    /**
     * 未结注单请求
     */
    @FormUrlEncoded
    @POST("order/find")
    fun openNoteEntity(@FieldMap partMap: Map<String, String>):Observable<BaseResponse<OpenNoteEntity>>

    /**
     * 撤单请求
     */
    @GET("order/cancelBet/{id}")
    fun delOpenNoteEntity(@Path("id")id:String):Observable<BaseResponse<String>>
    /**
     * 棋牌余额全部提取接口
     */
    @GET("chessCard/withdrawAll")
    fun getChessWithdrawAllUrl():Observable<BaseResponse<String>>

    @POST("common/getValidImage")
    fun getVerifyCode():Observable<BaseResponse<String>>

}

