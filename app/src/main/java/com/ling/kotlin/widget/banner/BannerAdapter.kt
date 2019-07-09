package com.ling.kotlin.widget.banner

import android.net.Uri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.ling.kotlin.R
import com.ling.kotlin.common.BannerEntity

class BannerAdapter(data: List<BannerEntity>?) :
    BaseQuickAdapter<BannerEntity, BaseViewHolder>(R.layout.banner_item, data) {

    override fun convert(helper: BaseViewHolder, item: BannerEntity) {
        val view = helper.getView<SimpleDraweeView>(R.id.banner_iv)
        view.setImageURI(Uri.parse(item.imageUrl),null)
    }
}
