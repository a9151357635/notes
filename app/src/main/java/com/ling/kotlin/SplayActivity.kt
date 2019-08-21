package com.ling.kotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.login.LoginActivity
import com.ling.kotlin.utils.AppUtils
import com.ling.kotlin.utils.CacheUtils
import kotlinx.android.synthetic.main.splay_layout.*

class SplayActivity(override val layoutId: Int = R.layout.splay_layout) :BaseActivity(){
    companion object{
        private const val PERMISSION_REQUEST_CODE = 0x10
    }
    override fun initView() {
        splay_version_tv.text = AppUtils.getVersionName()
        checkPermission()
    }
    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            val permissions = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.REQUEST_INSTALL_PACKAGES)
            val isCheckWrite = ContextCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_GRANTED
            val isCheckInstall = ContextCompat.checkSelfPermission(this,permissions[1]) == PackageManager.PERMISSION_GRANTED
            if(isCheckWrite && isCheckInstall){
                gotoMainOrLogin()
            }else{
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(),PERMISSION_REQUEST_CODE)
            }
        }else{
            gotoMainOrLogin()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode != PERMISSION_REQUEST_CODE)return
        if(permissions.isEmpty() || grantResults.isEmpty()){
            finish()
            return
        }
        if(Manifest.permission.WRITE_EXTERNAL_STORAGE == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            gotoMainOrLogin()
        }else if (Manifest.permission.REQUEST_INSTALL_PACKAGES == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            gotoMainOrLogin()
        }else{
            showToast(getString(R.string.app_no_permission_hint))
            finish()
        }
    }
    private fun gotoMainOrLogin(){
        startActivity(/*if(CacheUtils.getToken().isNullOrEmpty()) LoginActivity::class.java else */MainActivity::class.java)
        finish()
    }
}