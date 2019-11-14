package com.gcox.fansmeet.features.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.features.home.merchants.MerchantsFragment
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeScreenFragment : BaseFragment() {

    private var celebrities: ChildHomeFragment? = null
    private var influencers: ChildHomeFragment? = null
    private var merchants: ChildHomeFragment? = null
    private var events: ChildHomeFragment? = null
    private var tabsPagerAdapter: HomePagerAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(): HomeScreenFragment {
            return HomeScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        tabsPagerAdapter = HomePagerAdapter(childFragmentManager)
        viewPager.adapter = tabsPagerAdapter
//        tabsNewHome.setAllCaps(true)
//        tabsNewHome.setViewPager(viewPager)
        tabBar.setupWithViewPager(viewPager)
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                Timber.e("childFragmentManager %s", childFragmentManager.fragments)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    inner class HomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItemPosition(`object`: Any): Int {
            return super.getItemPosition(`object`)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return getString(R.string.home_tab_celebrities)
                1 -> return getString(R.string.home_tab_influencers)
                2 -> return getString(R.string.home_tab_merchants)
                3 -> return getString(R.string.home_tab_events)
            }
            return getString(R.string.home_tab_celebrities)
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> {
                    if (celebrities == null) {
                        celebrities = ChildHomeFragment.newInstance(HomeChildType.CELEBRITIES)
                    }
                    return celebrities
                }
                1 -> {
                    if (influencers == null) {
                        influencers = ChildHomeFragment.newInstance(HomeChildType.INFLUENCERS)
                    }
                    return influencers
                }
                2 -> {
                    if (merchants == null) {
                        merchants = ChildHomeFragment.newInstance(HomeChildType.MERCHANTS)
                    }
                    return merchants
                }

                3 -> {
                    if (events == null) {
                        events = ChildHomeFragment.newInstance(HomeChildType.EVENTS)
                    }
                    return events
                }
                else -> {
                }
            }
            return ChildHomeFragment.newInstance(HomeChildType.CELEBRITIES)
        }
    }
}