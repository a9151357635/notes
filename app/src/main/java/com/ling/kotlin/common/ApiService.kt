package com.ling.kotlin.common

import com.ling.kotlin.lottery.bean.HistoryEntity
import com.ling.kotlin.lottery.bean.LotteryEntity
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.PeriodTimeEntity
import com.ling.kotlin.retroft.BaseResponse
import com.ling.kotlin.utils.HeaderMapUtils
import io.reactivex.Observable
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
     * 棋牌余额全部提取接口
     */
    @GET("chessCard/withdrawAll")
    fun getChessWithdrawAllUrl(@HeaderMap headerMap:Map<String,String> = HeaderMapUtils.commonHeader()):Observable<BaseResponse<String>>
}