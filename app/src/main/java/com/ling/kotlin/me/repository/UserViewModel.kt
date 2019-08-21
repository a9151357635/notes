package com.ling.kotlin.me.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ling.kotlin.me.entity.UserInfoEntity
import com.ling.kotlin.retroft.viewmodel.BaseViewModel

class UserViewModel(application: Application) : BaseViewModel(application){
    private val repository by lazy { UserRepository(this) }

    fun findUserInfoEntity(): LiveData<UserInfoEntity> = repository.findUserInfoEntity()
}