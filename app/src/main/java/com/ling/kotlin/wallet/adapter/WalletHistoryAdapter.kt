package com.ling.kotlin.wallet.adapter

import android.util.SparseIntArray
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.wallet.bean.WalletHistoryEntity

/**
 * 钱包历史
 */
class WalletHistoryAdapter(data: List<WalletHistoryEntity>?) :
    BaseQuickAdapter<WalletHistoryEntity, BaseViewHolder>(R.layout.wallet_history_item, data) {

    private lateinit var sparseIntArray: SparseIntArray

    init {
        initIcon()
    }

    private fun initIcon() {
        sparseIntArray = SparseIntArray()
        sparseIntArray.put(1, R.drawable.ic_wallet_blue)
        sparseIntArray.put(2, R.drawable.ic_wallet_red)
        sparseIntArray.put(3, R.drawable.ic_wallet_yellow)
        sparseIntArray.put(4, R.drawable.ic_wallet_green)
        sparseIntArray.put(5, R.drawable.ic_wallet_orange)
    }

    override fun convert(helper: BaseViewHolder, item: WalletHistoryEntity) {
        val mTypeTv = helper.getView<TextView>(R.id.wallet_history_item_type_tv)
        mTypeTv.text = item.type
        mTypeTv.setCompoundDrawablesRelativeWithIntrinsicBounds(sparseIntArray.get(item.typeId), 0, 0, 0)
        val mMoneyTv = helper.getView<TextView>(R.id.wallet_history_item_money_tv)
        mMoneyTv.setTextColor(
            ContextCompat.getColor(
                mContext,
                if (item.money > 0) R.color.app_fount_color else R.color.app_text_main_normal_color
            )
        )
        mMoneyTv.text = mContext.getString(R.string.pay_history_money_hint, item.moneyStr)
        helper.setText(R.id.wallet_history_item_date_tv, item.createdTime)
        helper.setText(R.id.wallet_history_item_des_tv, item.description)
        helper.setText(R.id.wallet_history_item_type_tv, item.type)
    }
}

