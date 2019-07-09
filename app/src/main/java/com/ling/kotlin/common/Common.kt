package com.ling.kotlin.common

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ling.kotlin.retroft.BaseRemoteDataSource
import com.ling.kotlin.retroft.RequestCallback
import com.ling.kotlin.retroft.viewmodel.BaseViewModel
import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent


class CommonRemoteData(baseViewModelEvent: IBaseViewModelEvent) : BaseRemoteDataSource(baseViewModelEvent){

    fun requestBannerResponse(callback: RequestCallback<BannerResponse>){
        executeQuietly(getService().getBannerEntitys(),callback)
    }
    fun requestNoticeEntitys(param:Map<String,String> ,callback: RequestCallback<List<NoticeEntity>>){
        executeQuietly(getService().getNoticeEntitys(param),callback)
    }
}

class MainRepository(baseViewModelEvent: IBaseViewModelEvent){

    private val commonService = CommonRemoteData(baseViewModelEvent)
    val bannerList: MutableLiveData<List<BannerEntity>> = MutableLiveData()
    val noticeList: MutableLiveData<List<NoticeEntity>> = MutableLiveData()

    fun requestBannerRemote():LiveData<List<BannerEntity>>{
        commonService.requestBannerResponse(object : RequestCallback<BannerResponse> {
            override fun onSuccess(data: BannerResponse) {
                bannerList.value = data.imageList
            }
        })
        return bannerList
    }


    fun requestNoticeRemote(param: Map<String, String>):LiveData<List<NoticeEntity>>{
        commonService.requestNoticeEntitys(param,object : RequestCallback<List<NoticeEntity>> {
            override fun onSuccess(data: List<NoticeEntity>) {
                noticeList.value = data
            }
        })
        return noticeList
    }
}

class CommonViewModel(application: Application):BaseViewModel(application){
    private val repository by lazy { MainRepository(this) }

    fun requestBannerRemote(refresh: Boolean ):LiveData<List<BannerEntity>>{
        if(!refresh){
            return repository.bannerList
        }
        return repository.requestBannerRemote()
    }

    fun requestNoticeRemote(refresh: Boolean ):LiveData<List<NoticeEntity>>{
        if(!refresh){
            return repository.noticeList
        }
        return repository.requestNoticeRemote(mapOf("page" to "1","size" to "20"))
    }
}