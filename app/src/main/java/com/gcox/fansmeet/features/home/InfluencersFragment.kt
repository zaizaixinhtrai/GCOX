package com.gcox.fansmeet.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.fragment.BaseFragment

class InfluencersFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): InfluencersFragment {
            return InfluencersFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.influencers_fragment, container, false)
    }
}