package com.gcox.fansmeet.features.stars

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.features.stars.viewholders.StarsViewHolder
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.UiUtils
import kotlinx.android.synthetic.main.activity_stars.*
import org.koin.android.viewmodel.ext.android.viewModel

class StarsActivity : BaseToolBarActivity(), StarsViewHolder.OnClickListener, OnLoadMoreListenerRecyclerView {

    private val getGiftViewModel: StarsViewModel by viewModel()
    private var adapter: StarsAdapter? = null
    private var listEntries = arrayListOf<DisplayableItem>()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        showDialog(this, getString(R.string.connecting_msg))
        getGiftViewModel.getStarts(nextId)
    }

    override fun onResume() {
        super.onResume()
        handleTurnoffMenuSliding()
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_stars
    }

    override fun init() {
        setTopBarTile(getString(R.string.stars_title))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        observeData()
        createAdapter()
        rcvStars.setOnLoadMoreListener(this)
        UiUtils.setColorSwipeRefreshLayout(swiperefresh!!)
        swiperefresh!!.setOnRefreshListener { this.refreshData() }
    }

    private fun refreshData() {
        if (!CheckNetwork.isNetworkAvailable(this)) {
            swiperefresh?.isRefreshing = false
            return
        }
        swiperefresh?.isRefreshing = false
        showDialog(this, getString(R.string.connecting_msg))
        nextId = Constants.WEB_SERVICE_START_PAGE
        getGiftViewModel.getStarts(nextId)
    }

    private fun createAdapter() {
        rcvStars!!.addItemDecoration(UiUtils.ListSpacingItemDecoration(PixelUtil.dpToPx(this, 5), false))
        adapter = StarsAdapter(null, listEntries, this)
        rcvStars.adapter = adapter
    }

    private fun observeData() {
        getGiftViewModel.getError().observe(this, Observer {
            dismissDialog()
            swiperefresh?.isRefreshing = false
        })

        getGiftViewModel.getStart.observe(this, Observer {
            if (nextId == Constants.WEB_SERVICE_START_PAGE) {
                listEntries.clear()
            }
            listEntries.addAll(it?.historiesResponse?.result!!)
            adapter?.notifyDataSetChanged()
            txt_bean.text = it.balance.toString()
            dismissDialog()
            nextId = it.historiesResponse?.nextId!!
            isEnded = it.historiesResponse.isEnd!!
            swiperefresh?.isRefreshing = false
            checkIfNoData()
        })
    }

    private fun checkIfNoData() {
        if (listEntries.isNotEmpty()) {
            no_data_view?.visibility = View.GONE
            rcvStars.visibility = View.VISIBLE
        } else {
            no_data_view?.visibility = View.VISIBLE
            rcvStars.visibility = View.GONE
        }
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded) getGiftViewModel.getStarts(nextId)
        }
    }

    fun fakeData(): List<StarsModel> {
        val items = arrayListOf<StarsModel>()
        for (i in 1..30) {
            items.add(
                StarsModel(
                    "Trivia satr",
                    123, 100, ""
                )
            )
        }

        return items
    }
}