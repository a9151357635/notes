package com.ling.kotlin.lottery

import android.view.View
import android.view.ViewTreeObserver
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
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import com.ling.kotlin.utils.TimerUtils
import kotlinx.android.synthetic.main.bet_content_layout.*
import kotlinx.android.synthetic.main.lottery_bet_layout.*
import kotlinx.android.synthetic.main.bet_open_layout.*
import kotlinx.android.synthetic.main.bet_title_layout.*


class BetActivity(override val layoutId: Int = R.layout.lottery_bet_layout):BaseActivity(), View.OnClickListener {
    private lateinit var viewModel: LotteryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DrawerAdapter
    private lateinit var historyAdapter: LotteryHistoryAdapter
    private var lotteryId:Int = 0
    private lateinit var timerUtils: TimerUtils
    private var historyList:List<HistoryEntity> ? =null
    private var isExpend:Boolean = false
    private var curtNumber:Long = 0
    val menus = MutableLiveData<List<MenuEntity>>()
    private var menuList: List<MenuEntity>? = null
    override fun initView() {
        setUpWithHost()
        bet_back.setOnClickListener(this)
        bet_cur_note.setOnClickListener(this)
        bet_helper.setOnClickListener(this)
        bet_menu.setOnClickListener(this)
        bet_history_more_iv.setOnClickListener(this)
        initHistoryView()
        initDrawerView()
        viewModel = getViewModel(LotteryViewModel::class.java)
        initData()

    }

    private fun initHistoryView(){
        historyAdapter = LotteryHistoryAdapter(null)
        bet_history_rv.adapter = historyAdapter
    }
    private fun initDrawerView() {
        recyclerView = bet_nav_view.getHeaderView(0).findViewById(R.id.bet_drawer_rv)
        adapter = DrawerAdapter(null)
        adapter.setOnItemClickListener { adapter, _, position ->
            bet_drawer_layout.closeDrawer(GravityCompat.START)
            val entity = adapter.getItem(position) as LotteryEntity
            bet_menu.text = entity.lotteryName
            lotteryId = entity.lotteryId
            timerUtils.cancel()
            curtNumber = 0L
            getCurPeriodTime()
            menus.postValue(entity.menuDetails)
        }
        recyclerView.adapter = adapter
    }

    private  fun setUpWithHost(){
        bet_vp.adapter = TabsPagerAdapter(supportFragmentManager, listOf(AreaFragment(),ChatFragment()), arrayOf("投注区","聊天区"))
        bet_tab.setupWithViewPager(bet_vp)
    }

    private fun initData(){
        val bundle = intent?.extras
        bet_menu.text = bundle?.getString("lotteryName")
        lotteryId = bundle?.getInt("lotteryId",0) ?:0
        menuList = bundle?.getParcelableArrayList("menuList")
        menuList?.let {
            menus.postValue(it)
        }
        getLotteryEntitys()
        timerUtils = TimerUtils((10..16).random() * 1000.toLong(), 1000, object : TimerUtils.TimerTaskListener {
            override fun run() {
               getLotteryHistorys()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bet_back -> finish()
            R.id.bet_cur_note -> gotoCurNoteActivity()
            R.id.bet_menu -> openDrawLayout()
            R.id.bet_history_more_iv -> showMore()
        }
    }

    override fun onPause() {
        super.onPause()
        curtNumber = 0L
        timerUtils.cancel()
    }
    override fun onResume() {
        super.onResume()
        getCurPeriodTime()
    }

    private fun getLotteryEntitys(){
        viewModel.getLotteryEntitys(true).observe(this, Observer {
            val followList = it.filter { entity -> entity.isFollow}
            val hotList = it.filter { entity -> entity.isHot == 1}
            if(followList.isNullOrEmpty()){
                adapter.setNewData(listOf(DrawerEntity("热门",1,hotList)))
            }else{
                adapter.setNewData(listOf(DrawerEntity("关注",0,followList),DrawerEntity("热门",1,hotList)))
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
            bet_period_tv.text = it.curPeriodNum.toString()
            curtNumber = it.curPeriodNum
            val blockTime = it.blockTime
            val convertRemainTime = it.convertRemainTime
            if(convertRemainTime > 0 ){
                bet_period_hint_tv.text = "开奖倒计时:"
                bet_countdown.start(convertRemainTime * 1000)
                bet_countdown.setOnCountdownEndListener {countdown->
                    startBlockTimer(countdown, blockTime)
                }
            }else{
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
            }
        })
    }

    private fun openDrawLayout(){
        if (bet_drawer_layout.isDrawerOpen(GravityCompat.START)) bet_drawer_layout.closeDrawer(GravityCompat.START) else bet_drawer_layout.openDrawer(GravityCompat.START)
    }
    private fun gotoCurNoteActivity(){}

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
}