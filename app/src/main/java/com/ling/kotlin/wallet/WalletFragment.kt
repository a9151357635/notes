package com.ling.kotlin.wallet

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.base.Callback
import com.ling.kotlin.utils.AnimUtils
import com.ling.kotlin.wallet.adapter.ChartAdapter
import com.ling.kotlin.wallet.adapter.WalletHistoryAdapter
import com.ling.kotlin.wallet.bean.ChartEntity
import com.ling.kotlin.wallet.bean.WalletEntity
import com.ling.kotlin.wallet.bean.WalletHistoryEntity
import kotlinx.android.synthetic.main.wallet_home_layout.view.*

class WalletFragment(override val layoutId: Int = R.layout.wallet_home_layout) : BaseFragment(), Callback,
    View.OnClickListener {

    private lateinit var chartAdapter: ChartAdapter
    private lateinit var historyAdapter: WalletHistoryAdapter
    private lateinit var mTotalMoney: TextView
    private lateinit var mAvailableMoney: TextView
    private lateinit var mTodayMoney: TextView
    private lateinit var mUnavailableMoney: TextView
    private lateinit var presenter: WalletPresenter

    override fun initView(v: View) {
        mTotalMoney = v.wallet_home_total_balance_tv as TextView
        mAvailableMoney = v.wallet_home_available_tv as TextView
        mTodayMoney = v.wallet_home_today_tv as TextView
        mUnavailableMoney = v.wallet_home_unavailable_tv as TextView
        initRecycleView(v)
        initHistoryRecycleView(v)
        v.wallet_home_refresh_iv.setOnClickListener(this)
        v.wallet_home_pay_tv.setOnClickListener(this)
        v.wallet_home_withdraw_tv.setOnClickListener(this)
        v.wallet_home_convert_tv.setOnClickListener(this)
        v.wallet_home_my_tv.setOnClickListener(this)

        presenter = WalletPresenter(context, this)

    }

    private fun initRecycleView(v: View) {
        chartAdapter = ChartAdapter(null)
        v.wallet_home_chart_rv.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        v.wallet_home_chart_rv.adapter = chartAdapter
    }

    private fun initHistoryRecycleView(v: View) {
        historyAdapter = WalletHistoryAdapter(null)
        historyAdapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position) as WalletHistoryEntity
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_wallet_to_wallet_detail_activity,  bundleOf("detailData" to data))
            }
        }
        addFooterView()
        v.wallet_home_history_rv.layoutManager = LinearLayoutManager(context)
        v.wallet_home_history_rv.adapter = historyAdapter

    }

    private fun addFooterView() {
        var footerView = LayoutInflater.from(context).inflate(R.layout.wallet_histroy_footer_layout, null)
        historyAdapter.addFooterView(footerView)
        footerView.setOnClickListener {
            view?.let { Navigation.findNavController(it).navigate(R.id.action_wallet_to_wallet_history_activity) }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.wallet_home_refresh_iv -> refreshRequest()
            R.id.wallet_home_my_tv -> gotoMyWalletActivity()
            R.id.wallet_home_pay_tv -> gotoPayActivity()
            R.id.wallet_home_withdraw_tv -> gotoWithdrawActivity()
            R.id.wallet_home_convert_tv -> gotoConvertActivity()

        }
    }

    override fun onResume() {
        super.onResume()
        refreshRequest()
    }

    /**
     * 刷新数据
     */
    private fun refreshRequest() {
        getWalletEntityData()
        getChatData()
    }

    private fun getChatData() {
        presenter.getChartData("chart_url")

    }

    private fun getWalletEntityData() {
        presenter.getWalletEntity("wallet_entity")
    }


    override fun onSuccess(data: Any, url: String) {
        if (url.contains("chart_url")) {
            chartAdapter.setNewData(data as List<ChartEntity>)
        } else if (url.contains("wallet_entity")) {
            var walletEntity = data as WalletEntity

            AnimUtils.startAnimator(mTotalMoney, walletEntity.totalMoney)
            AnimUtils.startAnimator(mAvailableMoney, walletEntity.availableMoney)
            AnimUtils.startAnimator(mTodayMoney, walletEntity.todayMoney)
            AnimUtils.startAnimator(mUnavailableMoney, walletEntity.unavailableMoney)
            historyAdapter.setNewData(walletEntity.walletHistory)
        }
    }

    override fun onError(error: String, errorCode: Int, url: String) {
    }

    private fun gotoMyWalletActivity() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_to_wallet_my_activity) }
    }

    private fun gotoWithdrawActivity() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_to_wallet_withdraw_activity) }
    }

    private fun gotoPayActivity() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_wallet_to_wallet_pay_activity) }
    }

    private fun gotoConvertActivity() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_wallet_to_wallet_convert_activity) }
    }
}