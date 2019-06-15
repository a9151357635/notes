package com.ling.kotlin.wallet

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.Callback
import com.ling.kotlin.wallet.adapter.WalletHistoryAdapter
import com.ling.kotlin.wallet.bean.WalletHistoryEntity
import kotlinx.android.synthetic.main.wallet_history_layout.*

class WalletHistoryActivity : BaseActivity(), Callback, View.OnClickListener{

    override fun onClick(v: View?) {
//        if (!isFinishing) {
//            TimePickerViewUtils.getInstance().showOptionsPicker()
//        }
//        break
    }

    private lateinit var adapter: WalletHistoryAdapter
    private lateinit var walletPresenter: WalletPresenter
    override val layoutId: Int = R.layout.wallet_history_layout

    override fun initView() {

        initTitleView("钱包记录", "类型", true, true, this)
        wallet_history_rv.layoutManager = LinearLayoutManager(this)
        adapter = WalletHistoryAdapter(null)
        adapter.setOnItemClickListener { adapter, view, position ->
            var  data = adapter?.getItem(position) as WalletHistoryEntity
            val  intent = Intent(this,WalletDetailActivity::class.java)
            intent.putExtra("data",data)
            startActivity(intent)
        }
        wallet_history_rv.adapter = adapter
        walletPresenter = WalletPresenter(this,this)
        request()
    }

    private fun request(){
        walletPresenter.getHistoryData("wallet_history")
    }

    override fun onError(error: String, errorCode: Int, url: String) {
    }

    override fun onSuccess(data: Any, url: String) {
        if(url.contains("wallet_history")){
            adapter.setNewData(data as List<WalletHistoryEntity>)
        }
    }
}
