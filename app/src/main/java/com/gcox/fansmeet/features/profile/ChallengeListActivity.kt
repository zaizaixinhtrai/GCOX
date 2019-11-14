package com.gcox.fansmeet.features.profile

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity

class ChallengeListActivity : BaseToolBarActivity() {

    private var celebrityProfileFragment: ChallengeListFragment? = null
    private var userId: Int? = 0

    companion object {
        @JvmStatic
        fun newIntent(context: Context, userId: Int): Intent {
            val intent = Intent(context, ChallengeListActivity::class.java)
            intent.putExtra(Constants.USER_PROFILE_ID, userId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent?.extras?.getInt(Constants.USER_PROFILE_ID)
        if (savedInstanceState == null && userId != null) {
            celebrityProfileFragment = ChallengeListFragment.newInstance(userId!!)
            showFullFragmentScreen(celebrityProfileFragment)
        }
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
    }

    override fun getLayoutContentId(): Int {
        return 0
    }

    override fun onResume() {
        super.onResume()
//        handleTurnoffMenuSliding()
//        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
    }

    override fun init() {
        handleTurnoffMenuSliding()
    }
}