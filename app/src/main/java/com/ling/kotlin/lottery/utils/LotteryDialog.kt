package com.ling.kotlin.lottery.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseDialogFragment
import com.ling.kotlin.base.CommonDialog
import com.ling.kotlin.common.CommonDialogEntity
import com.ling.kotlin.lottery.adapter.ChatSureAdapter
import com.ling.kotlin.lottery.adapter.ChipAdapter
import com.ling.kotlin.lottery.adapter.LotterySureAdapter
import com.ling.kotlin.lottery.bean.ChipEntity
import com.ling.kotlin.lottery.bean.LotteryInfoEntity
import com.ling.kotlin.lottery.bean.SureEntity
import com.ling.kotlin.lottery.bean.SureInfoEntity
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import com.ling.kotlin.utils.*
import kotlinx.android.synthetic.main.lottery_bet_helper_dialog.view.*
import kotlinx.android.synthetic.main.lottery_bet_sure_dialog.view.*
import kotlinx.android.synthetic.main.lottery_chip_dialog.view.*

/**
 * 助手
 */
class HelperDialog(override val layoutId: Int = R.layout.lottery_bet_helper_dialog, override val gravity: Int = Gravity.TOP or Gravity.RIGHT):BaseDialogFragment(),
    View.OnClickListener {
    private var lotteryId = 0

    override fun initView(v: View) {
        v.lottery_helper_result_tv.setOnClickListener(this)
        v.lottery_helper_basic_tv.setOnClickListener(this)
        v.lottery_helper_desc_tv.setOnClickListener(this)
        v.lottery_helper_induction_tv.setOnClickListener(this)
        v.lottery_helper_sound_tv.setOnClickListener(this)
        v.lottery_helper_night_tv.setOnClickListener(this)
        lotteryId = arguments?.getInt("lotteryId")?:0
        val todayMoney = arguments?.getDouble("todayMoney")?:0.0
        v.lottery_helper_today_money_tv.text = todayMoney.toString()
        HelperLiveData.observe(this, Observer {

        })
        val leftDrawable = if(CacheUtils.isOpenSound())R.drawable.ic_helper_sound_open else R.drawable.ic_helper_sound_close
        v.lottery_helper_sound_tv.setCompoundDrawablesWithIntrinsicBounds( leftDrawable,0,0,0)
        IsInductionLiveData.observe(this, Observer {
            val leftDrawable = if(it) R.drawable.ic_helper_induction_open else R.drawable.ic_helper_induction_close
            v.lottery_helper_induction_tv.setCompoundDrawablesWithIntrinsicBounds(leftDrawable,0,0,0)
        })
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.lottery_helper_result_tv -> gotoHelperActivity(1)
            R.id.lottery_helper_basic_tv -> gotoHelperActivity(2)
            R.id.lottery_helper_desc_tv -> gotoHelperActivity(3)
            R.id.lottery_helper_induction_tv -> isInduction()
            R.id.lottery_helper_sound_tv -> isOpenSound()
            R.id.lottery_helper_night_tv -> handlerHelper(3)
        }
    }

    private fun gotoHelperActivity(type:Int){

    }

    private fun handlerHelper(type:Int){
        when(type){
            2 -> OpenSoundLiveData.postValue(true)
        }
    }

    private fun isOpenSound(){
        if(!CacheUtils.isOpenSound()){
            CacheUtils.saveOpenSound(true)
            view?.lottery_helper_sound_tv?.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_helper_sound_close,0,0,0)
        }else{
            CacheUtils.saveOpenSound(false)
            view?.lottery_helper_sound_tv?.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_helper_sound_open,0,0,0)
        }
        dismissAllowingStateLoss()
    }

    private fun isInduction(){
        val isOpen = IsInductionLiveData.value?:false
        IsInductionLiveData.postValue(!isOpen)
        dismissAllowingStateLoss()
    }
}



/**
 * 筹码对话框
 */
class ChipDialogFragment(override val layoutId: Int = R.layout.lottery_chip_dialog) : BaseDialogFragment(),
    View.OnClickListener {
    private val localList by lazy { CacheUtils.getChips() }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lottery_chip_cancel_tv -> dismissAllowingStateLoss()
            R.id.lottery_chip_sure_tv -> {
                localList.sort()
                CacheUtils.saveChip(localList)
                dismissAllowingStateLoss()
                ChipLiveData.postValue(true)
            }
        }
    }

    private val adapter = ChipAdapter(null)
    override val windowWidth: Int
        get() = (DisplayUtils.getWindowWidth(activity) / 1.4).toInt()
    override fun initView(v: View) {
        adapter.setOnItemClickListener { adapter, _, position ->
            val chipEntity = adapter.getItem(position) as ChipEntity
            itemClick(chipEntity)
        }
        v.lottery_chip_rv.adapter = adapter
        v.lottery_chip_cancel_tv.setOnClickListener(this)
        v.lottery_chip_sure_tv.setOnClickListener(this)
        initData()
    }

    private fun itemClick(chipEntity: ChipEntity) {
        if (chipEntity.selectId == 1) {
            if (localList?.size == 3) {
                return
            }
            localList.remove(chipEntity.number)
        } else {
            localList.add(chipEntity.number)
            if (localList.size > 3) {
                localList.removeAt(0)
            }
        }
        adapter.setSelectList(localList)
        adapter.notifyDataSetChanged()
    }

    private fun initData() {
        val data = listOf(
            ChipEntity(1, 0),
            ChipEntity(5, 0),
            ChipEntity(10, 0),
            ChipEntity(50, 0),
            ChipEntity(100, 0),
            ChipEntity(500, 0),
            ChipEntity(1000, 0),
            ChipEntity(5000, 0),
            ChipEntity(10000, 0),
            ChipEntity(50000, 0)
        )

        adapter.setSelectList(localList)
        adapter.setNewData(data)
    }
}

/**
 * 投注基础类
 */
abstract class  BettingDialog(override val layoutId: Int = R.layout.lottery_bet_sure_dialog):BaseDialogFragment(){
    protected abstract val mAdapter: BaseQuickAdapter<* ,BaseViewHolder>
    protected abstract fun convertData(input: Double): Map<String,String>
    protected abstract fun updateUI(input: Double)
    protected abstract fun notifys()
    protected abstract fun initData(v: View)
    protected abstract fun combineSize():Int
    protected abstract fun isCanBet():Boolean
    protected abstract val lotteryId: Int
    lateinit var inputEt:EditText
    private lateinit var totalMoneyTv:TextView
    protected lateinit var viewModel: LotteryViewModel
    override fun initView(v: View) {
        inputEt = v.lottery_sure_number_et
        totalMoneyTv = v.lottery_sure_total_amount_tv
        mAdapter.addFooterView(LayoutInflater.from(context).inflate(R.layout.lottery_bet_sure_dialog_footer_layout, null))
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view?.id){
                R.id.lottery_sure_item_close_tv  ->{
                    adapter.remove(position)
                    setTotalMoney()
                    if(adapter.data.isNullOrEmpty()){
                        inputEt.setText("")
                        dismissAllowingStateLoss()
                    }
                }
            }
        }
        v.lottery_sure_rv.adapter = mAdapter
        val tw = if (AppUtils.isYLCOrCXC()) DecimalInputTextWatcher(inputEt,DecimalInputTextWatcher.Type.decimal,2) else DecimalInputTextWatcher(inputEt, 7)
        tw.setListener(object : DecimalInputTextWatcher.onTextWatcherListener {
            override fun afterTextChanged(s: String) {
                if ("null" != s) {
                    setInput(s.toDoubleOrNull() ?: 0.0)
                }
            }
        })
        //投注金额变化监听
        inputEt.addTextChangedListener(tw)
        //关闭对话框
        v.lottery_sure_close_iv.setOnClickListener {
            KeyboardUtils.hideSoftInput(inputEt)
            dismissAllowingStateLoss()
        }
        //确认投注
        v.lottery_sure_bt.setOnClickListener {
            betting()
        }
        viewModel = ViewModelProviders.of(this).get(LotteryViewModel::class.java)
        initData(v)
    }

    private fun setInput(input:Double){
        if(500000.0 - input < 0){
            val inputMax = 500000.0
            ToastUtils.showToast(msg = getString(R.string.lottery_single_money_max_hint))
            inputEt.setText(inputMax.toString())
            updateUI(inputMax)
            setTotalMoney()
            return
        }
        updateUI(input)
        setTotalMoney()
    }

     fun setTotalMoney() {
        val money = inputEt.text.toString().toDoubleOrNull()?:0.0
         totalMoneyTv.text = (combineSize() * money).toString()

    }
    /**
     * 投注
     */
    private fun betting() {
        if(!isAdded)return
        KeyboardUtils.hideSoftInput(inputEt)
        if(!isCanBet()){
            ToastUtils.showToast(msg = "正在开奖，请等待下一期")
            return
        }
        val input = inputEt.text.toString().toDoubleOrNull() ?: 0.0
        if (input <= 0.0) {
            ToastUtils.showToast(msg = getString(R.string.lottery_please_money_hint))
            return
        }
        val totalMoney = totalMoneyTv.text.toString().toDoubleOrNull()?:0.0
        if (totalMoney - 5000000.0 > 0) {
            ToastUtils.showToast(msg = getString(R.string.lottery_single_field_money_max_hint))
            return
        }
        viewModel.betting(lotteryId,convertData(input)).observe(this, Observer {msg ->
            showDialog(msg)
        })
    }

    private fun showDialog(msg:String){
        val commonDialogEntity = CommonDialogEntity(content = msg)
        showDialog(CommonDialog(object : CommonDialog.DialogListener {
            override fun onClick(content: String) {
                notifys()
                dismissAllowingStateLoss()
            }
        }).apply {
            isCancelable = false
            arguments = bundleOf("dialogEntity" to commonDialogEntity) },"CommonDialog")
    }
}

/**
 * 投注确认对话框
 */
class LotterySureDialog(override val mAdapter: BaseQuickAdapter<LotteryInfoEntity,BaseViewHolder> = LotterySureAdapter(null)):BettingDialog() {

    private var lotteryIds = 0

    override fun initData(v: View) {
        val money = arguments?.getDouble("inputMoney", 0.0) ?: 0.0
        inputEt.setText(money.toString())
        updateUI(money)
        val combineList = CombineEntityLiveData.value ?: return
        val selectList = mutableListOf<LotteryInfoEntity>()
        selectList.addAll(combineList)
        lotteryIds = selectList[0].lotteryId
        mAdapter.setNewData(selectList)
        v.lottery_sure_name_tv.text = arguments?.getString("lotteryName")
        CurPeriodNumLiveData.observe(this, Observer {
            v.lottery_sure_period_tv.text = getString(R.string.lottery_period_hint, it.toString())
        })
        setTotalMoney()
    }

    override val lotteryId: Int
        get() = lotteryIds

    override fun updateUI(input: Double) {
        (mAdapter as LotterySureAdapter).setMoney(input.toString())
        mAdapter.notifyDataSetChanged()
    }

    override fun notifys() {
        //通知清空选中的数据
        ClearLiveData.postValue(true)
        //刷新余额
        viewModel.getWalletBalance()
    }

    override fun combineSize(): Int {
        val it = mAdapter.data as List<LotteryInfoEntity>
        val size = it.size
        return if (size == 1) {
            if (it[0].combineSize > 1) it[0].combineSize else it.size
        } else {
            size
        }
    }

    override fun isCanBet(): Boolean {
        return BetCanLiveData.value ?: false
    }

    override fun convertData(input: Double): Map<String, String> {
        val array = JsonArray()
        mAdapter.data.forEach { entity ->
            val obj = JsonObject()
            obj.addProperty("money", input)
            obj.addProperty("playId", entity.playId)
            if (entity.type == LotteryInfoEntity.ONLY_ONE) {
                val betInfo = entity.convertPlayId ?: entity.convertName
                obj.addProperty("betInfo", betInfo)
            }
            array.add(obj)
        }
        return mapOf("PeriodNumber" to CurPeriodNumLiveData.value.toString(), "data" to array.toString())
    }
}


/**
 * 聊天室跟单对话框
 */
class ChatSureDialog(override val mAdapter: BaseQuickAdapter<SureInfoEntity, BaseViewHolder> = ChatSureAdapter(null)) : BettingDialog() {
    private var periodNumber: String? = null
    private var lotteryIdInt = 0
    private var combineSizeInt = 0
    override fun initData(v: View) {
        val dataStr = arguments?.getString("data") ?: return
        val sureEntity = JSonUtils.parseObject(dataStr, SureEntity::class.java) ?: return
        val sureInfo = sureEntity.data[0]
        combineSizeInt = sureInfo.ZuShu
        periodNumber = sureEntity.PeriodNumber
        lotteryIdInt = sureEntity.lotteryId.toInt()
        inputEt.setText(sureInfo.money.toString())
        updateUI(sureInfo.money)
        mAdapter.setNewData(sureEntity.data)
        v.lottery_sure_name_tv.text = sureEntity.lotteryName
        v.lottery_sure_period_tv.text = periodNumber
        setTotalMoney()
    }

    override fun updateUI(input: Double) {
        (mAdapter as ChatSureAdapter).setMoney(input.toString())
        mAdapter.notifyDataSetChanged()
    }

    override fun combineSize(): Int {
        val it = mAdapter.data as List<SureInfoEntity>
        return if(combineSizeInt > 1) combineSizeInt else it.size
    }
    override val lotteryId: Int get() = lotteryIdInt

    override fun convertData(input: Double): Map<String, String> {
        val array = JsonArray()
        mAdapter.data.forEach { entity ->
            val obj = JsonObject()
            obj.addProperty("money", input)
            obj.addProperty("playId", entity.playId)
            obj.addProperty("PlayName", entity.playName)
            obj.addProperty("betInfo", entity.betInfo)
            obj.addProperty("Odds", entity.odds)
            obj.addProperty("totalMoney", entity.totalMoney)
            obj.addProperty("orderdetail", entity.orderdetail)
            array.add(obj)
        }
        val number = periodNumber ?: ""
        return mapOf("PeriodNumber" to number, "data" to array.toString())
    }

    override fun notifys() {
        //刷新余额
        viewModel.getWalletBalance()
    }
    override fun isCanBet(): Boolean {
        return true
    }
}

