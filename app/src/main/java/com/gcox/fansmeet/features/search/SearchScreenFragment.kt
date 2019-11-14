package com.gcox.fansmeet.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.fragment.BaseFragment

class SearchScreenFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): SearchScreenFragment {
            return SearchScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}