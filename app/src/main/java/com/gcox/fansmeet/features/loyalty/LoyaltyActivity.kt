package com.gcox.fansmeet.features.loyalty

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.features.loyalty.viewholders.LoyaltyViewHolder
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.UiUtils
import kotlinx.android.synthetic.main.activity_loyalty.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoyaltyActivity : BaseToolBarActivity(), LoyaltyViewHolder.OnClickListener, OnLoadMoreListenerRecyclerView {

    private var adapter: LoyaltyAdapter? = null
    private var listEntries = arrayListOf<DisplayableItem>()
    private val getLoyaltyViewModel: LoyaltyViewModel by viewModel()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        observeData()
        getLoyaltyViewModel.getLoyalty(nextId, Constants.PAGE_LIMITED)
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_loyalty
    }

    override fun init() {
        handleTurnoffMenuSliding()
        setTopBarTile(getString(R.string.stars_loyalty))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        createAdapter()
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
    }

    private fun refreshData() {
        if (CheckNetwork.isNetworkAvailable(applicationContext)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            swiperefresh?.isRefreshing = false
            showDialog(this, "")
            getLoyaltyViewModel.getLoyalty(nextId, Constants.PAGE_LIMITED)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this
            )
        }
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded) getLoyaltyViewModel.getLoyalty(nextId, Constants.PAGE_LIMITED)
        }
    }

    private fun createAdapter() {
        rcvLoyalty!!.addItemDecoration(UiUtils.ListSpacingItemDecoration(PixelUtil.dpToPx(this, 5), false))
        adapter = LoyaltyAdapter(null, listEntries, this)
        rcvLoyalty.adapter = adapter
        rcvLoyalty.setOnLoadMoreListener(this)
    }

    private fun observeData() {
        getLoyaltyViewModel.getError().observe(this, Observer {
            dismissDialog()
            rcvLoyalty.setLoading(false)
            swiperefresh?.isRefreshing = false
        })

        getLoyaltyViewModel.getLoyalty.observe(this, Observer {
            if (nextId == Constants.WEB_SERVICE_START_PAGE) listEntries.clear()
            listEntries.addAll(it?.historiesEntity?.result!!)
            adapter?.notifyDataSetChanged()
//            txt_bean.text = it.balance.toString()
            dismissDialog()
            rcvLoyalty.setLoading(false)
            swiperefresh?.isRefreshing = false
            checkIfNoData()
            isEnded = it.historiesEntity.isEnd!!
            nextId = it.historiesEntity.nextId!!
        })
    }

    private fun checkIfNoData() {
        if (listEntries.isNotEmpty()) {
            no_data_view?.visibility = View.GONE
            rcvLoyalty.visibility = View.VISIBLE
        } else {
            no_data_view?.visibility = View.VISIBLE
            rcvLoyalty.visibility = View.GONE
        }
    }

    fun fakeData(): List<LoyaltyModel> {
        val items = arrayListOf<LoyaltyModel>()
        for (i in 1..30) {
            items.add(
                LoyaltyModel(
                    "Manny Pacquiao",
                    123, 123, "18 Mar 2019, 11:43"
                )
            )
        }

        return items
    }
}