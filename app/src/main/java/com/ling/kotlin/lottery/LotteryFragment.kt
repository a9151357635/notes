package com.ling.kotlin.lottery

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseFragment
import com.ling.kotlin.base.TabsPagerAdapter
import com.ling.kotlin.common.CommonViewModel
import com.ling.kotlin.widget.banner.BannerAdapter
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import com.ling.kotlin.utils.TimerUtils
import com.ling.kotlin.widget.marquee.MarqueeView
import com.ling.kotlin.widget.marquee.SimpleMF
import kotlinx.android.synthetic.main.lottery_layout.view.*

class LotteryFragment(override val layoutId:Int = R.layout.lottery_layout) : BaseFragment() {

    private lateinit var viewModel: LotteryViewModel
    private lateinit var comViewModel: CommonViewModel
    private lateinit var timerUtils: TimerUtils
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var marquees:MarqueeView<TextView,String>
    override fun initView(v: View) {
        val fragments = mutableListOf(HotFragment(),AllFragment(),FollowFragment())
        v.lotteryVp.adapter = TabsPagerAdapter(childFragmentManager, fragments as List<Fragment>?,resources.getStringArray(R.array.lottery_id))
        v.lotteryTab.setupWithViewPager(v.lotteryVp)
        bannerAdapter = BannerAdapter(null)
        v.marqueesView.setTextEllipsize(TextUtils.TruncateAt.END)
        v.marqueesView.setTextSingleLine(true)
        v.marqueesView.setTextGravity(Gravity.CENTER)
        v.marqueesView.setTextSize(15f)
        marquees = v.marqueesView as MarqueeView<TextView,String>
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onResume() {
        super.onResume()
        setUpViewModel()
        setUpFromMainView()
    }

    override fun onPause() {
        super.onPause()
        timerUtils.cancel()
        view?.banner?.setAutoPlaying(false)
    }

    private fun init() {
        timerUtils = TimerUtils(20000, 1000, object : TimerUtils.TimerTaskListener {
            override fun run() {
                setUpViewModel()
            }
        })
        viewModel = getViewModel(LotteryViewModel::class.java)
        activity?.let {
            comViewModel = ViewModelProviders.of(it).get(CommonViewModel::class.java)
        }
    }


    private fun setUpViewModel(){
        viewModel.getLotteryEntitys().observe(this, Observer {
            timerUtils.start()
        })

//        viewModel.getActivity().observe(this, Observer {
//            it?.let {
//                ToastUtils.showToast(msg = it)
//            }
//        })

    }
    private fun setUpFromMainView(){

        activity?.let { it ->
            comViewModel.requestBannerRemote(false).observe(it, Observer {
                bannerAdapter.setNewData(it)
                view?.banner?.setAdapter(bannerAdapter)
                view?.banner?.setAutoPlaying(true)
            })
            comViewModel.requestNoticeRemote(false).observe(it, Observer {
                val dataList = ArrayList<String>()
                for (data in it){
                    dataList.add(data.noticeContent.trim())
                }
                val marqueeFactory = context?.let { it1 -> SimpleMF<String>(it1)}
                marqueeFactory?.setNewDataList(dataList)
                marqueeFactory?.let { it1 ->
                    marquees.setMarqueeFactory(it1)
                    marquees.startFlipping()
                }
            })
        }

    }
}