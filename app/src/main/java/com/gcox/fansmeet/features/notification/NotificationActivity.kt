package com.appster.features.notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gcox.fansmeet.R

import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_notify.*

import javax.inject.Inject

/**
 * Created by User on 11/2/2015.
 */
class NotificationActivity : BaseToolBarActivity(), HasSupportFragmentInjector {

    private var isEditProfile: Boolean = false

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var mTabsPagerAdapter: TabsPagerNewsAdapter? = null

    internal var notifyYou: NotifyFragment? = null
    internal var notifyFollowing: NotifyFragment? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutContentId(): Int {
        return R.layout.fragment_notify
    }

    override fun init() {
        setFragment()
    }

    private fun setFragment() {
        mTabsPagerAdapter = TabsPagerNewsAdapter(supportFragmentManager)
        pagerNotify.adapter = mTabsPagerAdapter
        tabsNotify.shouldExpand = true
        tabsNotify.setViewPager(pagerNotify)
        pagerNotify.offscreenPageLimit = 2
    }

    override fun onResume() {
        super.onResume()
        setTopBarTile(getString(R.string.notification))
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        handleTurnoffMenuSliding()
    }

    override fun onBackPressed() {
        if (isEditProfile) {
            val intent = Intent()
            intent.putExtra(ConstantBundleKey.BUNDLE_EDIT_ABLE_POST, true)
            setResult(RESULT_OK, intent)

        } else {
            setResult(RESULT_CANCELED)
        }

        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) return
        if (data != null) {
            isEditProfile = data.getBooleanExtra(ConstantBundleKey.BUNDLE_EDIT_ABLE_POST, true)
        }

    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }
    }

    private inner class TabsPagerNewsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(index: Int): Fragment? {

            when (index) {
                0 -> {
                    if (notifyYou == null) {
                        notifyYou = NotifyFragment.newInstance(NotifyFragment.NotifyType.FanConnect)
                    }
                    return notifyYou
                }
                1 -> {
                    if (notifyFollowing == null) {
                        notifyFollowing = NotifyFragment.newInstance(NotifyFragment.NotifyType.CelebConnect)
                    }
                    return notifyFollowing
                }
                else -> {
                }
            }

            return null
        }

        override fun getCount(): Int {
            // get item count - equal to number of tabs
            return 2
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence? {
            val tabs = arrayOf(getString(R.string.notification_you), getString(R.string.notification_following))
            return tabs[position]
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }
}
