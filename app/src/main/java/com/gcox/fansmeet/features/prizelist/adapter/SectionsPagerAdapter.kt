package com.gcox.fansmeet.features.prizelist.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gcox.fansmeet.R
import com.gcox.fansmeet.features.prizelist.PrizeFragment
import com.gcox.fansmeet.features.prizelist.models.BoxesModel

//private val TAB_TITLES = arrayOf(
//    R.string.price_tab_normal,
//    R.string.price_tab_special,
//    R.string.price_tab_premium,
//    R.string.price_tab_exclusive
//)

class SectionsPagerAdapter(private val listTab: List<BoxesModel>, private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return PrizeFragment.newInstance(listTab[position].id)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTab[position].name
    }

    override fun getCount(): Int {
        return listTab.size
    }
}