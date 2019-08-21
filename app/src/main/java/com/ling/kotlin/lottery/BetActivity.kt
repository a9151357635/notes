package com.ling.kotlin.lottery

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import cn.iwgang.countdownview.CountdownView
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.TabsPagerAdapter
import com.ling.kotlin.lottery.adapter.DrawerAdapter
import com.ling.kotlin.lottery.adapter.LotteryHistoryAdapter
import com.ling.kotlin.lottery.bean.*
import com.ling.kotlin.lottery.utils.*
import com.ling.kotlin.lottery.viewmodel.*
import com.ling.kotlin.utils.TimerUtils
import kotlinx.android.synthetic.main.bet_content_layout.*
import kotlinx.android.synthetic.main.bet_open_layout.*
import kotlinx.android.synthetic.main.bet_title_layout.*
import kotlinx.android.synthetic.main.lottery_bet_layout.*


class BetActivity(override val layoutId: Int = R.layout.lottery_bet_layout):BaseActivity(), View.OnClickListener {
    private val soundPoolUtils by lazy { SoundPoolUtils(this) }
    private lateinit var viewModel: LotteryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var drawerBalanceTv:TextView
    private lateinit var adapter: DrawerAdapter
    private lateinit var historyAdapter: LotteryHistoryAdapter
    private lateinit var betView:BetView
    private var lotteryId:Int = 0
    private lateinit var timerUtils: TimerUtils
    private var historyList:List<HistoryEntity> ? =null
    private var isExpend:Boolean = false
    private var curtNumber:Long = 0
    private var lotteryName:String ?= null
    private var todayMoney:Double?= null
    private var isOpen = false
    val menus = MutableLiveData<List<MenuEntity>>()
    override fun initView() {
        setUpWithHost()
        bet_back.setOnClickListener(this)
        bet_cur_note.setOnClickListener(this)
        bet_helper.setOnClickListener(this)
        bet_menu.setOnClickListener(this)
        bet_history_more_iv.setOnClickListener(this)
        initHistoryView()
        initDrawerView()
        initBetView()
        viewModel = getViewModel(LotteryViewModel::class.java)
        initData()

    }

    private fun initHistoryView(){
        historyAdapter = LotteryHistoryAdapter(null)
        bet_history_rv.adapter = historyAdapter
    }
    private fun initDrawerView() {
        drawerBalanceTv = bet_nav_view.getHeaderView(0).findViewById(R.id.bet_drawer_balance)
        recyclerView = bet_nav_view.getHeaderView(0).findViewById(R.id.bet_drawer_rv)
        adapter = DrawerAdapter(null)
        adapter.setOnItemClickListener { adapter, _, position ->
            bet_drawer_layout.closeDrawer(GravityCompat.START)
            val entity = adapter.getItem(position) as? LotteryEntity?:return@setOnItemClickListener
            bet_menu.text = entity.lotteryName
            lotteryName = entity.lotteryName
            lotteryId = entity.lotteryId
            timerUtils.cancel()
            curtNumber = 0L
            getCurPeriodTime()
            menus.postValue(entity.menuDetails)
            betView.setSize(0)
        }
        recyclerView.adapter = adapter
    }

    private  fun setUpWithHost(){
        bet_vp.adapter = TabsPagerAdapter(supportFragmentManager, listOf(AreaFragment(),ChatFragment()), arrayOf("投注区","聊天区"))
        bet_tab.setupWithViewPager(bet_vp)
    }

    private fun initBetView(){
        betView = lottery_bet_view as BetView
        betView.setFragmentManger(supportFragmentManager)

        ChipLiveData.observe(this, Observer {
            betView.setData()
        })
        //清空通知
        ClearLiveData.observe(this, Observer {
            betView.setSize(0)
        })
    }
    private fun initData(){
        val bundle = intent?.extras
        lotteryId = bundle?.getInt("lotteryId",0) ?:0
        getLotteryEntitys()
        //拉取开奖结果倒计时
        timerUtils = TimerUtils((10..16).random() * 1000.toLong(), 1000, object : TimerUtils.TimerTaskListener {
            override fun run() {
               getLotteryHistorys()
            }
        })
        //选中组合数
        CombineEntityLiveData.observe(this, Observer {

            if(it.isNullOrEmpty()){
                betView.setSize(0)
                return@Observer
            }
            betView.setSize(if(it[0].type == LotteryInfoEntity.ONLY_ONE) it[0].combineSize else it.size)

        })
        BetLiveData.observe(this, Observer { input ->
            CombineEntityLiveData.value?:return@Observer
            showDialog(LotterySureDialog().apply {arguments = bundleOf("lotteryName" to lotteryName, "inputMoney" to input)},"LotterySureDialog")

        })
        BetCanLiveData.observe(this, Observer {
            isOpen = it
        })
        viewModel.getVerifyCode()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bet_back -> finish()
            R.id.bet_cur_note -> gotoOpenNoteActivity()
            R.id.bet_menu -> openDrawLayout()
            R.id.bet_history_more_iv -> showMore()
            R.id.bet_helper -> showBetHelper()
        }
    }

    private fun gotoOpenNoteActivity(){
        val intent = Intent(this,OpenNoteActivity::class.java)
        intent.putExtras(bundleOf("lotteryId" to lotteryId))
        startActivity(intent)
    }

    private fun openDrawLayout(){
        if (bet_drawer_layout.isDrawerOpen(GravityCompat.START)) bet_drawer_layout.closeDrawer(GravityCompat.START) else bet_drawer_layout.openDrawer(GravityCompat.START)
    }

    private fun showMore(){
        if(isExpend){
            isExpend = false
            bet_history_more_iv.setImageResource(R.drawable.ic_down)
            historyAdapter.setNewData( listOf(historyList?.get(0)))
        }else{
            isExpend = true
            bet_history_more_iv.setImageResource(R.drawable.ic_up)
            historyAdapter.setNewData(historyList)
        }
    }

    private fun showBetHelper(){
        showDialog(HelperDialog().apply { arguments = bundleOf("lotteryId" to lotteryId,"todayMoney" to todayMoney) },"helper")
    }
    override fun onPause() {
        super.onPause()
        curtNumber = 0L
        timerUtils.cancel()
    }
    override fun onResume() {
        super.onResume()
        getCurPeriodTime()
        getWalletBalance()
    }

    override fun onDestroy() {
        CombineEntityLiveData.postValue(null)
        soundPoolUtils.stop()
        super.onDestroy()
    }
    private fun getLotteryEntitys(){
        //得到数据共享(彩种集合)
        LotteryEntityLiveData.observe(this, Observer {
            val followList = it.filter { entity -> entity.isFollow}
            val hotList = it.filter { entity -> entity.isHot == 1}
            if(followList.isNullOrEmpty()){
                adapter.setNewData(listOf(DrawerEntity("热门",1,hotList)))
            }else{
                adapter.setNewData(listOf(DrawerEntity("关注",0,followList),DrawerEntity("热门",1,hotList)))
            }
            //得到根据lotteryId查找实体，然后做相关操作
           val lotteryEntity =  it.find { entity -> entity.lotteryId == lotteryId }
            bet_menu.text = lotteryEntity?.lotteryName
            lotteryName = lotteryEntity?.lotteryName
            lotteryEntity?.menuDetails?.let {menuList ->
                menus.postValue(menuList)
            }
        })
    }

    private fun getCurPeriodTime(){
        bet_countdown.stop()
        bet_countdown.allShowZero()
        viewModel.getCurPeriodTime(lotteryId,1).observe(this, Observer {
            //网络环境差的情况下，拉取数据不及时，而Livedata会先把上次缓存的数据直接给到页面
            if(curtNumber == it.curPeriodNum){
                return@Observer
            }
            BetCanLiveData.postValue(true)
            bet_period_tv.text = it.curPeriodNum.toString()
            curtNumber = it.curPeriodNum
            val blockTime = it.blockTime
            val convertRemainTime = it.convertRemainTime
            if(convertRemainTime > 0 ){
                bet_period_hint_tv.text = "截至:"
                bet_countdown.start(convertRemainTime * 1000)
                bet_countdown.setOnCountdownIntervalListener(1000) { _, remainTime ->
                    if (remainTime in 8500..11000 && isOpen) {
                        soundPoolUtils.playBetCaveat()
                    }
                }
                bet_countdown.setOnCountdownEndListener {countdown->
                    BetCanLiveData.postValue(false)
                    startBlockTimer(countdown, blockTime)
                }
            }else{
                BetCanLiveData.postValue(false)
                startBlockTimer(bet_countdown,(blockTime + convertRemainTime))
            }
            getLotteryHistorys()
        })
    }
    private fun startBlockTimer(countdown: CountdownView, blockTime: Long) {
        bet_period_hint_tv.text = "正在开奖:"
        countdown.start(blockTime * 1000)
        countdown.setOnCountdownEndListener {
            getCurPeriodTime()
        }
    }

    private fun getLotteryHistorys(){
        timerUtils.cancel()
        viewModel.getLotteryHistorys(lotteryId, mapOf("page" to "1","size" to "5")).observe(this, Observer {
            historyList = it
            historyAdapter.setNewData(if(isExpend) it else listOf(it[0]))
            //是否开启倒计时
            if( curtNumber - it[0].expectNumber >=2){
                timerUtils.start()
                return@Observer
            }
            //播放声音
            soundPoolUtils.playBetOpen()
        })
    }

    private fun getWalletBalance(){
        viewModel.getWalletBalance().observe(this, Observer {
            betView.setBanlance(it.lotteryBalance)
            todayMoney = it.todayWinMoney
            drawerBalanceTv.text = getString(R.string.app_balance_hint,it.lotteryBalance.toString())
        })
    }
}
