package com.ling.kotlin.login.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ling.kotlin.login.entity.LoginEntity
import com.ling.kotlin.lottery.repository.LoginRepository
import com.ling.kotlin.retroft.viewmodel.BaseViewModel

class LoginViewModel(application: Application) : BaseViewModel(application)  {
    private val repository by lazy { LoginRepository(this) }

    fun register(param: Map<String, Any>):LiveData<LoginEntity> = repository.register(param)

    fun login(param: Map<String, Any>):LiveData<LoginEntity> = repository.login(param)
}