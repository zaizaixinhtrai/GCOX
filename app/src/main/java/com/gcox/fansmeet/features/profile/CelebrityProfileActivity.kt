package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.activity.BaseToolBarActivity

class CelebrityProfileActivity : BaseToolBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
    }

    override fun onResume() {
        super.onResume()
        handleTurnoffMenuSliding()
    }

    override fun getLayoutContentId(): Int {
        return 0
    }

    override fun init() {
    }
}