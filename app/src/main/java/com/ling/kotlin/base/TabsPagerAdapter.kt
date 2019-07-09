package com.ling.kotlin.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * tab页面的适配器
 */
class TabsPagerAdapter
/**
 * titles和fragment集合数量要相同
 * @param fm
 * @param fragments
 * @param titles
 */
    (fm: FragmentManager, private val fragmentList: List<Fragment>?, private val titles: Array<String>) :
    FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragmentList?.get(position)!!
    }

    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
    }
    /**
     * Convert zero-based numbering of tabs into readable numbering of tabs starting at 1.
     *
     * @param position - Zero-based tab position
     * @return Readable tab position
     */
    private fun getReadableTabPosition(position: Int): Int {
        return position + 1
    }
}
