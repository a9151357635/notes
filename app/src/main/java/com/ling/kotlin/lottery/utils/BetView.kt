package com.ling.kotlin.lottery.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ling.kotlin.R
import com.ling.kotlin.base.CommonDialog
import com.ling.kotlin.common.CommonDialogEntity
import com.ling.kotlin.lottery.adapter.ChipAdapter
import com.ling.kotlin.lottery.bean.ChipEntity
import com.ling.kotlin.utils.*
import kotlinx.android.synthetic.main.lottery_bet_view.view.*


class BetView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle),
    View.OnClickListener {
    private val MAXMONEY = 500000.0
    private lateinit var inputView: LinearLayout
    private lateinit var inputEd: EditText
    private lateinit var balanceTv: TextView
    private lateinit var moneyTv: TextView
    private lateinit var sizeTv: TextView
    private lateinit var chipAdapter:ChipAdapter
    private lateinit var supportFragmentManager: FragmentManager
    private var size:Int = 0
    private var balance:Double =0.0;
    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView(){
        inputView = lottery_bet_input_parent
        inputEd = lottery_bet_input_ed
        balanceTv = lottery_bet_balance_tv
        moneyTv = lottery_bet_money_tv
        sizeTv = lottery_bet_size_tv
        lottery_chip_set_tv.setOnClickListener(this)
        lottery_bet_reset_tv.setOnClickListener(this)
        lottery_bet_sure_tv.setOnClickListener(this)
        chipAdapter = ChipAdapter(null)
        chipAdapter.setOnItemClickListener { adapter, _, position ->
            val chipEntity = adapter.getItem(position) as ChipEntity
            changChips(chipEntity.number.toDouble())
        }
        lottery_bet_chip_rv.adapter = chipAdapter
        val  tw = if (AppUtils.isYLCOrCXC()) DecimalInputTextWatcher(inputEd,DecimalInputTextWatcher.Type.decimal,2) else DecimalInputTextWatcher(inputEd,7)
        tw.setListener(object: DecimalInputTextWatcher.onTextWatcherListener {
            override fun afterTextChanged(s: String) {
                if("null" != s){
                    setInput(s.toDoubleOrNull()?:0.0)
                }
            }
        })
        //投注金额变化监听
        inputEd.addTextChangedListener(tw)
        setData()
    }

    fun setBanlance(balance: Double?){
        this.balance = balance?:0.0
        AppUtils.changeTextColor(context,"#5989ea",R.string.lottery_balance_hint,balance.toString(),balanceTv)
    }

    fun setFragmentManger(supportFragmentManager:FragmentManager){
        this.supportFragmentManager = supportFragmentManager
    }
     fun setData() {
         chipAdapter?.let {
             it.setSelectList(CacheUtils.getChips())
             it.setNewData(
                 listOf(
                     ChipEntity(CacheUtils.getChips()[0], 0),
                     ChipEntity(CacheUtils.getChips()[1], 0),
                     ChipEntity(CacheUtils.getChips()[2], 0)
                 )
             )
         }

    }

    fun setSize(size:Int){
        this.size = size
        inputView.visibility = if(size > 0) View.VISIBLE else View.GONE
        val inputDouble = inputEd.text.toString().trim().toDoubleOrNull()?:0.0
        sizeTv.text =  context?.getString(R.string.lottery_select_hint,size)
        moneyTv.text = AppUtils.decimalFormat((size.toDouble() * inputDouble).toString())
    }

    private fun changChips(number: Double){
        var input = inputEd.text.toString().toDoubleOrNull()?:0.0
        if(MAXMONEY - input < 0){
            ToastUtils.showToast(msg = context.getString(R.string.lottery_single_money_max_hint))
            return
        }
        var maxValue = input + number
        if(MAXMONEY - maxValue < 0){
            maxValue = MAXMONEY
            ToastUtils.showToast(msg = context.getString(R.string.lottery_single_money_max_hint))
        }
        inputEd.setText(maxValue.toString())
    }

    private fun setInput(input:Double){
        if(MAXMONEY - input < 0){
            val inputMax = 500000.0
            ToastUtils.showToast(msg = context.getString(R.string.lottery_single_money_max_hint))
            inputEd.setText(inputMax.toString())
            moneyTv.text =  AppUtils.decimalFormat((size.toDouble() * inputMax).toString())
            return
        }
        moneyTv.text =  AppUtils.decimalFormat((size.toDouble() * input).toString())
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lottery_bet_reset_tv -> {
                inputEd.setText("")
                setSize(0)
                ClearLiveData.postValue(true)
            }
            R.id.lottery_bet_sure_tv -> {
                onSure()
            }
            R.id.lottery_chip_set_tv -> {
                DialogUtils.showDialog(ChipDialogFragment(),supportFragmentManager,"chipDialog")
            }
        }
    }

    /**
     * 确认投注
     */
    private fun onSure() {
        val input = inputEd.text.toString().toDoubleOrNull() ?: 0.0
        if (input <= 0.0) {
            ToastUtils.showToast(msg = context.getString(R.string.lottery_please_money_hint))
            return
        }
        val totalMoney = moneyTv.text.toString().toDoubleOrNull()?:0.0
        if (balance - totalMoney < 0) {
            val commonDialogEntity = CommonDialogEntity(
                content = context?.getString( R.string.lottery_money_max_balance_hint, totalMoney.toString(), balance.toString()),
                sureHint = "去充值",
                isShowCancel = true
            )
            DialogUtils.showDialog(CommonDialog(object : CommonDialog.DialogListener {
                override fun onClick(content: String) {
                    //do somting
                }
            }).apply { arguments = bundleOf("dialogEntity" to commonDialogEntity) }, supportFragmentManager,"CommonDialog")
            return
        }
        if (totalMoney - 5000000.0 > 0) {
            ToastUtils.showToast(msg = context.getString(R.string.lottery_single_field_money_max_hint))
            return
        }
        BetLiveData.postValue(input)
    }
}

