package com.ling.kotlin.retroft

import com.ling.kotlin.common.ApiService

import com.ling.kotlin.retroft.viewmodel.IBaseViewModelEvent
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

/**
 * 基础远程拉取数据资源基类
 */
open class BaseRemoteDataSource(private val baseViewModelEvent:IBaseViewModelEvent){


    protected fun getService(): ApiService = getService(
        ApiService::class.java,
        HttpConfig.BASE_URL_MAP
    )

    protected fun <T : Any> getService(clz: Class<T>,host:String = HttpConfig.BASE_URL_MAP): T {
        return RetrofitManagement.instance.getService(clz,host)
    }

    protected fun <T> execute(observable: Observable<BaseResponse<T>>, callback: RequestCallback<T>){

        execute(observable, BaseSubscriber(callback),false)
    }
    protected fun <T> executeQuietly(observable: Observable<BaseResponse<T>>, callback: RequestCallback<T>){

        execute(observable, BaseSubscriber(callback),true)
    }
    private fun <T> execute(observable: Observable<BaseResponse<T>>, observer : Observer<OptionT<T>>, quietly:Boolean){

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if(!quietly){
                    showLoading()
                }
            }.doFinally { dismissLoading() }
            .flatMap(object :Function<BaseResponse<T>,ObservableSource<OptionT<T>>>{
                override fun apply(t: BaseResponse<T>): ObservableSource<OptionT<T>> {

                    when{
                        t.code == HttpConfig.CODE_SUCCESS || t.msg == "OK" ->{
                            val optionT = OptionT(t.data)
                            return createData(optionT)
                        }
                        else ->{
                            throw ServerResultException(t.msg ?: "未知错误", t.code)
                        }
                    }
                }
            }).subscribeWith(observer)
    }

    private fun <T> createData(t:T):Observable<T>{
        return Observable.create { emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            }catch (e:Exception){
                emitter.onError(e)
            }
        }
    }

    private fun showLoading(){
        baseViewModelEvent.showLoading()
    }
    private fun dismissLoading(){
        baseViewModelEvent.dismissLoading()
    }
 }