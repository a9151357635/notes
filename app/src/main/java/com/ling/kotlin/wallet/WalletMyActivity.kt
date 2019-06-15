package com.ling.kotlin.wallet

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.Callback
import com.ling.kotlin.wallet.adapter.WalletHistoryAdapter
import com.ling.kotlin.wallet.bean.WalletHistoryEntity
import com.ling.kotlin.wallet.bean.WalletMyEntity
import kotlinx.android.synthetic.main.wallet_my_header_layout.*
import kotlinx.android.synthetic.main.wallet_my_layout.*

/**
 * 我的钱包
 */
class WalletMyActivity : BaseActivity(), Callback{

    private lateinit var adapter: WalletHistoryAdapter
    private lateinit var presenter: WalletPresenter

    override val layoutId: Int = R.layout.wallet_my_layout

    override fun initView() {
        initTitleView("我的钱包")
        wallet_my_history_rv.layoutManager = LinearLayoutManager(this)
        adapter = WalletHistoryAdapter(null)
        adapter.setOnItemClickListener { adapter, view, position ->
            var  data = adapter?.getItem(position) as WalletHistoryEntity
            val  intent = Intent(this,WalletDetailActivity::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        }
        addFooterView()
        wallet_my_history_rv.adapter = adapter
        presenter = WalletPresenter(this,this)
        request()
    }

    private fun addFooterView(){
        var  footerView = LayoutInflater.from(this).inflate(R.layout.wallet_histroy_footer_layout,null)
        adapter.addFooterView(footerView)
        footerView.setOnClickListener {
            var  intent = Intent(this,WalletHistoryActivity::class.java)
            startActivity(intent)
        }
    }
    private fun request(){
        presenter.getMyWalletData("wallet_my")
    }

    override fun onSuccess(data: Any, url: String) {
        if(url.contains("wallet_my")){
            val  walletMyEntity = data as WalletMyEntity
            wallet_my_total_tv.text = walletMyEntity.totalMoney
            wallet_my_happy_tv.text = walletMyEntity.happyMoney
            wallet_my_ky_tv.text = walletMyEntity.kyMoney
            setTextColor(wallet_my_today_money_tv, walletMyEntity.totalMoney)
            setTextColor(wallet_my_happy_income_tv, walletMyEntity.happyIncome)
            setTextColor(wallet_my_ky_income_tv, walletMyEntity.kyIncome)
            adapter.setNewData(walletMyEntity.walletHistory)
        }
    }

    override fun onError(error: String, errorCode: Int, url: String) {

    }

    private fun setTextColor(textView: TextView, number: String) {
        var number = number
        if (TextUtils.isEmpty(number)) {
            number = "0"
        }
        val numberF = java.lang.Float.valueOf(number)
        if (numberF <= 0) {
            textView.text = number
            textView.setTextColor(ContextCompat.getColor(this, R.color.app_ae1d22_color))
        } else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.app_fount_color))
            textView.text = "+$number"
        }
    }
}
