package com.ling.kotlin.lottery

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.ling.kotlin.R
import com.ling.kotlin.base.BaseActivity
import com.ling.kotlin.base.CommonDialog
import com.ling.kotlin.common.CommonDialogEntity
import com.ling.kotlin.lottery.adapter.OpenNoteAdapter
import com.ling.kotlin.lottery.bean.OpenNoteInfoEntity
import com.ling.kotlin.lottery.viewmodel.LotteryViewModel
import kotlinx.android.synthetic.main.lottery_open_note_layout.*

/**
 * 未结注单
 */
class OpenNoteActivity(override val layoutId: Int = R.layout.lottery_open_note_layout): BaseActivity() {
    private val mAdapter by lazy { OpenNoteAdapter(null) }
    private lateinit var viewModel: LotteryViewModel
    private var lotteryId:Int = 0
    override fun initView() {
        initTitleView(title = "未结注单")
        open_note_rv.adapter = mAdapter
        viewModel = getViewModel(LotteryViewModel::class.java)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val infoEntity = adapter.getItem(position) as OpenNoteInfoEntity
            when(view.id){
                R.id.open_note_item_cancel_btn -> showDelDialog(infoEntity.id.toString(),position)
            }
        }
        lotteryId = intent?.extras?.getInt("lotteryId",0)?:0
    }

    override fun onResume() {
        super.onResume()
        requestOpenNoteEntity()
    }

    private fun requestOpenNoteEntity(){
        viewModel.openNoteEntity(lotteryId,1).observe(this, Observer {
            mAdapter.setNewData(it)
        })
    }

    private fun showDelDialog(id:String,position: Int){
        val commonEntity = CommonDialogEntity(
            content = getString(R.string.lottery_bet_del_content_hint),
            isShowCancel = true
        )
        showDialog(CommonDialog(object : CommonDialog.DialogListener {
            override fun onClick(content: String) {
              delOpenNoteEntity(id,position)
            }
        }).apply {
            isCancelable = false
            arguments = bundleOf("dialogEntity" to commonEntity) },"CommonDialog")
    }

    private fun delOpenNoteEntity(id:String,position:Int){
        viewModel.delOpenNoteEntity(id).observe(this, Observer {
            showToast(it)
            mAdapter.remove(position)
            mAdapter.notifyDataSetChanged()
        })
    }

}