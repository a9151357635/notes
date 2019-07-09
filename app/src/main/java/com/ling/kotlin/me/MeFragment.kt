package com.ling.kotlin.me

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import kotlinx.android.synthetic.main.me_layout.view.*

class MeFragment(override val layoutId:Int = R.layout.me_layout): BaseFragment(), View.OnClickListener {

    private lateinit var mLevelTitleTv:TextView
    private lateinit var mLevelTv:TextView

    override fun initView(v: View) {
        v.me_home_setting_iv.visibility = View.INVISIBLE
        v.me_home_setting_iv.setOnClickListener(this)
        v.me_home_icon_iv.setOnClickListener(this)
        v.me_home_wallet_tv.setOnClickListener(this)
        v.me_home_history_wait_tv.setOnClickListener(this)
        v.me_home_history_winning_tv.setOnClickListener(this)
        v.me_home_history_not_winning_tv.setOnClickListener(this)
        v.me_home_user_account_center_cl.setOnClickListener(this)
        v.me_home_user_level_cl.setOnClickListener(this)
        v.me_home_user_message_cl.setOnClickListener(this)
        v.me_home_user_server_cl.setOnClickListener(this)
        v.me_home_user_night_cl.setOnClickListener(this)
        v.me_home_user_version_cl.setOnClickListener(this)
        v.me_home_user_clear_cl.setOnClickListener(this)
        v.me_home_user_about_cl.setOnClickListener(this)
    }

    private fun setLevelData(levelTitle: String, level: Int) {
        mLevelTitleTv.text = levelTitle
        mLevelTv.text = "VIP$level"
        when(level){
            in 1..10 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_one)
            in 11..20 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_two)
            in 21..30 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_three)
            in 31..40 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_four)
            in 41..50 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_five)
            in 51..60 ->  mLevelTv.setBackgroundResource(R.drawable.ic_me_vip_six)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
//            R.id.me_home_setting_iv -> gotoSettingActivity()
            R.id.me_home_wallet_tv -> gotoMyWalletActivity()
            R.id.me_home_history_wait_tv -> gotoBetHistoryActivity("0")
            R.id.me_home_history_winning_tv -> gotoBetHistoryActivity("1")
            R.id.me_home_history_not_winning_tv -> gotoBetHistoryActivity("2")
            R.id.me_home_user_account_center_cl -> gotoAccountCenterActivity()
            R.id.me_home_icon_iv -> gotoAccountCenterActivity()
            R.id.me_home_user_level_cl -> gotoLevelActivity()
            R.id.me_home_user_server_cl -> gotoServerActivity()
            R.id.me_home_user_message_cl -> gotoMessageActivity()
            R.id.me_home_user_about_cl -> gotoAboutActivity()
            R.id.me_home_user_night_cl -> checkNight()
            R.id.me_home_user_clear_cl -> clearCache()
            R.id.me_home_user_version_cl -> checkVersion()
        }
    }
    private fun gotoMyWalletActivity(){
        view?.let { Navigation.findNavController(it).navigate(R.id.action_me_to_wallet_my_activity) }
    }
    private fun gotoBetHistoryActivity(type:String){
        view?.let { Navigation.findNavController(it).navigate(R.id.action_me_to_lotteryHistoryActivity,bundleOf("type" to type)) }
    }
    private fun gotoAccountCenterActivity(){
        view?.let { Navigation.findNavController(it).navigate(R.id.action_me_to_accountSecurityActivity) }
    }

    private fun gotoServerActivity(){

    }
    private fun gotoAboutActivity(){

    }
    private fun gotoMessageActivity(){

    }
    private fun gotoLevelActivity(){

    }
    private fun checkNight(){
        AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        activity?.recreate()

    }
    private fun clearCache(){

    }
    private fun checkVersion(){

    }

}