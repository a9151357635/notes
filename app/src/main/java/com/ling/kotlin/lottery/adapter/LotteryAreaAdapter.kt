package com.ling.kotlin.lottery.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ling.kotlin.R
import com.ling.kotlin.lottery.bean.LotteryGroupInfoEntity
import com.ling.kotlin.lottery.bean.LotteryInfoEntity
import com.ling.kotlin.lottery.utils.*
import com.ling.kotlin.utils.CombineUtils
import com.ling.kotlin.utils.ToastUtils
import com.ling.kotlin.widget.GridItemDecoration
import java.util.*
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

/**
 * 投注区域适配器
 */
class LotteryAreaAdapter(dataList: List<LotteryGroupInfoEntity>?) :
    BaseQuickAdapter<LotteryGroupInfoEntity, BaseViewHolder>(R.layout.lottery_area_item, dataList) {
    private val soundPoolUtils by lazy { SoundPoolUtils(mContext) }
    private val selectList = mutableListOf<LotteryInfoEntity>()
    //组合后的新集合
    val combineList = mutableListOf<LotteryInfoEntity>()
    private var menuId:String?=null
    //随机数
    private var randomPosition = -1
    fun setMenuId(menuId:String){
        this.menuId = menuId
    }

    fun setRandomPosition(randomPosition:Int){
        this.randomPosition = randomPosition
    }
    override fun setNewData(data: List<LotteryGroupInfoEntity>?) {
        selectList.clear()
        combineList.clear()
        super.setNewData(data)
    }

    override fun convert(helper: BaseViewHolder, item: LotteryGroupInfoEntity) {
        var infoEntityList = convertRandomPosition(helper.layoutPosition,item)
        val nameTv = helper.getView<TextView>(R.id.lottery_area_item_type_tv)
        nameTv?.text = item.name
        val mContentRv = helper.getView<RecyclerView>(R.id.lottery_area_item_content_rv)
        mContentRv.layoutManager = if (item.itemType == LotteryGroupInfoEntity.MARK_SIX) LinearLayoutManager(
            mContext,
            RecyclerView.VERTICAL,
            false
        ) else GridLayoutManager(mContext, if (item.lotteryId in LotteryUtils.sixMarkIdList) 3 else 2)

        val contentAdapter = when {
            item.itemType == LotteryGroupInfoEntity.MARK_SIX -> AreaMarkSixAdapter(infoEntityList)
            item.itemType == LotteryGroupInfoEntity.K_THREE -> AreaKThreeAdapter(infoEntityList)
            else -> AreaInfoAdapter(infoEntityList, item.itemType)
        }
        mContentRv.adapter = contentAdapter
        contentAdapter.setOnItemClickListener { adapter, view, position ->
            soundPoolUtils.playBetSelect()
            //播放选择声音
            val entity = adapter.getItem(position) as LotteryInfoEntity
            if (isSelectEntity(entity,helper.layoutPosition)) {
                adapter.notifyItemChanged(position)
                //重新设置赔率
                if( entity.playId in LotteryId.markSixNotInPlayId || entity.playId in  LotteryId.welfareThreePlayId || entity.playId in LotteryId.welfareSixPlayId){
                    nameTv?.text = item.name.plus(if(!selectList.isNullOrEmpty())"(赔率:${entity.convertOdds})" else "")
                }
                convertSelectList(selectList)
                onItemClickListener?.onItemClick(adapter, view, position)
            }
        }
    }

    private fun convertRandomPosition(position:Int,item: LotteryGroupInfoEntity):List<LotteryInfoEntity>{
        var infoEntityList = item.datas
        if(position == randomPosition){
            val infoGroupEntity = data[position]
            infoEntityList = infoGroupEntity.datas
            val infoEntity = infoEntityList[Random().nextInt(infoEntityList.size)]
            infoEntity.selectId = 1
            selectList.clear()
            selectList.add(infoEntity)
            convertSelectList(selectList)
            //通知选中数据更新
            CombineEntityLiveData.postValue(combineList)
        }
        randomPosition = -1
        return infoEntityList
    }

    /**
     * 是否选择或者删除Item
     */
    private fun isSelectEntity(entity: LotteryInfoEntity,groupPosition:Int): Boolean {
        if (entity.selectId == 0) {
            //最大值判断
            if(LotteryId.maxSelectMap.any { (key,value) ->selectList.size == key && entity.playId in value }){
                ToastUtils.showToast(msg = mContext.getString(R.string.lottery_select_max_hint, selectList.size))
                return false
            }
            //11选五直选系列
            if (entity.playId in LotteryId.straightPlayId && selectList.any { entity.number == it.number }) {
                ToastUtils.showToast(msg = mContext.getString(R.string.lottery_select_duplicate_hint))
                return false
            }
            entity.selectId = 1
            entity.groupPosition = groupPosition //一些特殊处理，比如11选五直选，做排列组合使用较多
            selectList.add(entity)
        } else {
            entity.selectId = 0
            selectList.remove(entity)
        }
        entity.convertOdds = convertOdds(entity.playId)

        return true
    }

    /**
     * 转换赔率
     */
    private fun convertOdds(playId:String):String{
        return when (selectList.size) {
            4 -> {
                 when (playId) {
                     in LotteryId.welfareSixPlayId -> "32.5"
                    else -> "--"
                }
            }
            5 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "12.95"
                    in LotteryId.welfareSixPlayId -> "13"
                    else -> "2.12"
                }
            }
            6 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "8.61"
                    in LotteryId.welfareSixPlayId -> "6.5"
                    else -> "2.63"
                }
            }
            7 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "6.15"
                in LotteryId.welfareSixPlayId -> "3.7"
                else -> "3.18"
            }
            8 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "4.61"
                in LotteryId.welfareSixPlayId -> "2.35"
                else -> "3.72"
            }
            9 -> {
                when (playId) {
                    in LotteryId.welfareThreePlayId -> "3.6"
                    else -> "4.5"
                }
            }
            10 -> when (playId) {
                in LotteryId.welfareThreePlayId -> "2.87"
                else -> "5.58"
            }
            11 -> "6.8"
            12 -> "8.5"
            else -> "--"
        }
    }


    private fun convertSelectList(selectList:List<LotteryInfoEntity>){
        combineList.clear()
        val selectSize = selectList.size
        if(selectSize <= 0){
            return
        }

        val playId = selectList[0].playId
        //根据最少选择数来做组合区分，返回0则表示不需要对选择的数据做排列组合
       val tempList = when {
            LotteryId.leastOneList.any { it == playId } -> {
                //只有一注，需要设置组合注数为1注
                val combineSize = when {
                    playId in LotteryId.markSixNotInPlayId  -> if(selectSize >= 5 ) 1 else 0
                    playId in LotteryId.welfareThreePlayId  -> if(selectSize >= 5 ) 1 else 0
                    playId in LotteryId.welfareSixPlayId  -> if(selectSize >= 4 ) 1 else 0
                    LotteryId.maxSelectMap.any { (key,value) ->selectList.size == key && playId in value } -> 1
                    else -> 0
                }
                combineOneList(selectList,1,combineSize)
            }
            LotteryId.leastTwoList.any { it == playId || it == menuId } -> {
                if(playId in  LotteryId.combineList){
                    convertCombineList(selectList)
                }else{
                    combineOneList(selectList,2, 1)
                }
            }
            LotteryId.leastThreeList.any { it == playId || it == menuId } -> {
                if(playId in  LotteryId.combineList){
                    convertCombineList(selectList)
                }else{
                    combineOneList(selectList,3, 1)
                }
            }
            LotteryId.leastFourList.any { it == playId || it == menuId } -> {
                combineOneList(selectList,4, 1)
            }
            LotteryId.leastFiveList.any { it == menuId } -> {
                combineOneList(selectList,5, 1)
            }
            else -> selectList
        }
        combineList.addAll(tempList)
    }

    /**
     * 只返回一个重新组合后的数据，一些特殊组合数
     * 根据后台需要把选中的集合做一个名称或者playId的拼接
     */
    private fun combineOneList(selectList: List<LotteryInfoEntity>,size:Int,combineSize:Int):List<LotteryInfoEntity>{
        if(combineSize == 0){
            return mutableListOf()
        }
        val convertPlayId = mutableListOf<String>()
        val convertName = mutableListOf<String>()
        val convertNameSb = StringBuilder()
        val convertPlaySb = StringBuilder()
        selectList.forEach{entity ->
            if(entity.name !in convertName){
                convertName.add(entity.name)
                convertNameSb.append(",")
                convertNameSb.append(entity.name)
            }
            if(entity.playId !in convertPlayId){
                convertPlayId.add(entity.playId)
                convertPlaySb.append(",")
                convertPlaySb.append(entity.playId)
            }
        }
        val entity = selectList[0]
        entity.convertName = if(convertName.size <=1) null else convertNameSb.toString().replaceFirst("," ,"")
        entity.convertPlayId =if(convertPlayId.size <=1)  null else convertPlaySb.toString().replaceFirst("," ,"")
        entity.combineSize = if(size == 1) combineSize else combine(selectList.size,size)
        entity.convertOdds = convertOdds(entity.playId)
        entity.type= LotteryInfoEntity.ONLY_ONE
        return listOf(entity)
    }

    /**
     * 组合数
     * @param combineSize
     * @param n
     * @return
     */
    private fun combine(combineSize:Int, n: Int): Int {
        //如果是组合数n==1或者n==1或者选中的数字小于组合数，都返回0
        return when {
            n == 0 || n == 1 || combineSize < n -> 0
            //C(7,3)=7*6*5/3*2*1 公式
            combineSize == n -> 1
            else  -> CombineUtils.combineSize(combineSize, n)
        }
    }

    /**
     * 从新组合数组
     * 根据选中中集合，重新拆分，通过排列顺序排列出一个新的组合数组
     */
    private fun convertCombineList(selectList: List<LotteryInfoEntity>):List<LotteryInfoEntity>{
        val combineOneList = selectList.filter { it.groupPosition == 0 }
        val combineTwoList = selectList.filter { it.groupPosition == 1 }
        val combineThreeList = selectList.filter { it.groupPosition == 2 }
        val combineNewList = arrayListOf<LotteryInfoEntity>()
        if(combineTwoList.isNullOrEmpty())return combineNewList
        combineOneList.forEach { oneEntity ->
            combineTwoList.forEach { twoEntity ->
                when {
                    oneEntity.playId == ElevenPlayId.STRAIGHT_THREE_PLAYID || oneEntity.playId in LotteryId.welfareThreeLocationList -> when {
                        combineThreeList.isNullOrEmpty() -> return combineNewList
                        else -> combineThreeList.forEach { threeEntity ->
                            oneEntity.convertName = oneEntity.name + "," + twoEntity.name + "," + threeEntity.name
                            oneEntity.combineSize = combineOneList.size * combineTwoList.size * combineThreeList.size
                            combineNewList.add(oneEntity)
                        }
                    }
                    else -> {
                        oneEntity.combineSize = combineOneList.size * combineTwoList.size
                        oneEntity.convertName = oneEntity.name + "," + twoEntity.name
                        combineNewList.add(oneEntity)
                    }
                }
            }
        }
        return combineNewList
    }
}

class AreaInfoAdapter(dataList: List<LotteryInfoEntity>?, private val menuType: Int) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.lottery_area_item_info_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.lottery_area_item_info_odds_tv)
        if (item.isNumber == 0) {
            nameTv.text = item.name
            nameTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        } else {
            nameTv.text = item.number.toString()
            nameTv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
        }
        oddsTv.text = item.odds
        oddsTv.visibility = if ("0" == item.odds) View.GONE else View.VISIBLE
        when (menuType) {
            LotteryGroupInfoEntity.NUMBER -> when {
                item.lotteryId in listOf(50, 55, 80, 81) -> {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                    LotteryUtils.numberMap[item.number]?.let { nameTv.setBackgroundResource(it) }
                }
                95 == item.lotteryId -> {
                    nameTv.text = ""
                    LotteryUtils.ballMap[item.number]?.let { nameTv.setBackgroundResource(it) }
                }
                else -> {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_main_another_normal_color))
                    nameTv.setBackgroundResource(R.drawable.ic_circle_white)
                }
            }
            LotteryGroupInfoEntity.SFY -> {
                if (item.isNumber == 1) {
                    nameTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                   val color =  LotteryUtils.colorMap[item.color]
                    if (color != null) {
                        nameTv.setBackgroundResource(color)
                    }else{
                        nameTv.setTextColor(ContextCompat.getColor(mContext,R.color.app_text_main_another_normal_color) )
                        nameTv.setBackgroundResource(R.drawable.ic_circle_white)
                    }
                }else{
                    nameTv.setBackgroundColor( ContextCompat.getColor(mContext,android.R.color.transparent))
                }
            }
        }
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, item.isNumber)
    }
}

class AreaMarkSixAdapter(dataList: List<LotteryInfoEntity>?) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_marksix_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.area_info_marksix_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.area_info_marksix_odds_tv)
        val numberRecyclerView = helper.getView<RecyclerView>(R.id.area_info_marksix_content_rv)
        nameTv.text = item.name
        oddsTv.text = item.odds
        numberRecyclerView.adapter = AreaMarkSixContentAdapter(item.color.split(","), item.belongNumbers.split(","))
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, 0)
    }
}

class AreaMarkSixContentAdapter(
    private val colors: List<String>,
    dataList: List<String>?
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.lottery_area_info_marksix_content_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        val numberTv = helper.getView<TextView>(R.id.area_info_marksix_content_tv)
        numberTv.text = item
        val position = helper.layoutPosition
        LotteryUtils.colorMap[colors[position]]?.let {
            numberTv.setBackgroundResource(it)
        }
    }
}

class AreaKThreeAdapter(dataList: List<LotteryInfoEntity>?) :
    BaseQuickAdapter<LotteryInfoEntity, BaseViewHolder>(R.layout.lottery_area_info_kthree_item, dataList) {
    override fun convert(helper: BaseViewHolder, item: LotteryInfoEntity) {
        val nameTv = helper.getView<TextView>(R.id.area_info_item_kthree_name_tv)
        val oddsTv = helper.getView<TextView>(R.id.area_info_item_kthree_odds_tv)
        val numberRecyclerView = helper.getView<RecyclerView>(R.id.area_info_item_kthree_rv)
        oddsTv.text = item.odds
        if (item.isNumber == 1) {
            nameTv.text = ""
            nameTv.visibility = View.GONE
            numberRecyclerView.visibility = View.VISIBLE
            numberRecyclerView.adapter = AreaKThreeNumberAdapter(nameToNumber(item.name))
        } else {
            nameTv.text = item.name
            nameTv.visibility = View.VISIBLE
            numberRecyclerView.visibility = View.GONE
        }
        BackgroundColorUtils.changeColor(mContext, helper.itemView, nameTv, oddsTv, item.selectId, item.isNumber)
    }

    /**
     * 名字转拆分转数组
     * @return
     */
    private fun nameToNumber(name: String): MutableList<String> {
        val names = mutableListOf<String>()
        if (Pattern.compile("[0-9]*").matcher(name).matches()) {
            val c = name.toCharArray()
            c.forEach {
                names.add(it.toString())
            }
        }
        return names
    }
}

class AreaKThreeNumberAdapter(dataList: List<String>?) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.lottery_area_info_kthree_number_item, dataList) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        LotteryUtils.dicemap[item?.toInt()]?.let { helper?.setBackgroundRes( R.id.kthree_number_tv, it) }
    }
}

object BackgroundColorUtils {
    /**
     * 设置选中的背景颜色 投注区域大量使用
     * @param mContext 上下文
     * @param selectId 是否选中
     * @param view 需要更改背景视图
     * @param mOddsTv 赔率
     * @param mNameTv 名字
     * @param isNumber 类型，是否需要改变名字的颜色 当是1的时候改变
     */
    fun changeColor(mContext: Context, view: View, mNameTv: TextView, mOddsTv: TextView, selectId: Int, isNumber: Int) {
        if (selectId == 1) {
            view.setBackgroundResource(R.drawable.shape_red_bg_drawale)
            mOddsTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_lottery_text_select_color))
            if (0 == isNumber) mNameTv.setTextColor( ContextCompat.getColor(mContext,R.color.app_lottery_text_select_color))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent))
            mOddsTv.setTextColor(ContextCompat.getColor(mContext, R.color.app_fount_color))
            if (0 == isNumber) mNameTv.setTextColor( ContextCompat.getColor(mContext,R.color.app_text_main_another_normal_color)
            )
        }
    }
}