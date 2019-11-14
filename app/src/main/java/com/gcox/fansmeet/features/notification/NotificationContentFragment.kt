package com.gcox.fansmeet.features.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.appster.features.notification.NotifyFragment
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.fragment.BaseFragment
import kotlinx.android.synthetic.main.notification_content_fragment.*
import timber.log.Timber

class NotificationContentFragment : BaseFragment() {

    private var mTabsPagerAdapter: TabsPagerNewsAdapter? = null
    internal var celebConnect: NotifyFragment? = null
    internal var fanConnect: NotifyFragment? = null
    private var gachaPrizes:NotifyFragment? = null
    val tabs by lazy { arrayOf(getString(R.string.notification_celeb_connect),
        getString(R.string.notification_fan_connect),getString(R.string.notification_gacha_prizes)) }

    companion object {

        @JvmStatic
        fun newInstance(): NotificationContentFragment {
            return NotificationContentFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.notification_content_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !areLecturesLoaded) {
            setFragment()
            areLecturesLoaded = true
        }
    }

    private fun setFragment() {
        mTabsPagerAdapter = TabsPagerNewsAdapter(childFragmentManager)
        pagerNotify.adapter = mTabsPagerAdapter
        tabsNotify.setupWithViewPager(pagerNotify)
        pagerNotify.offscreenPageLimit = 3
    }

    private inner class TabsPagerNewsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(index: Int): Fragment? {

            when (index) {
                0 -> {
                    if (celebConnect == null) {
                        celebConnect = NotifyFragment.newInstance(NotifyFragment.NotifyType.CelebConnect)
                    }
                    return celebConnect
                }
                1 -> {
                    if (fanConnect == null) {
                        fanConnect = NotifyFragment.newInstance(NotifyFragment.NotifyType.FanConnect)
                    }
                    return fanConnect
                }
                2 -> {
                    if (gachaPrizes == null) {
                        gachaPrizes = NotifyFragment.newInstance(NotifyFragment.NotifyType.GachaPrizes)
                    }
                    return gachaPrizes
                }
                else -> {
                }
            }

            return null
        }

        override fun getCount(): Int {
            // get item count - equal to number of tabs
            return tabs.size
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            return tabs[position]
        }
    }
}