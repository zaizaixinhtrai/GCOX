package com.gcox.fansmeet.features.prizelist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.core.itemdecorator.VerticalSpaceItemDecoration
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.rewards.adapter.PrizeListAdapter
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.UiUtils
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.webview.ActivityViewWeb
import kotlinx.android.synthetic.main.fragment_prize_list_item.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PrizeFragment : BaseFragment(), OnDisplayableItemClicked, OnLoadMoreListenerRecyclerView {


    val adapter by lazy {
        PrizeListAdapter(
            items = ArrayList<Prize>(),
            listener = this@PrizeFragment
        )
    }
    private var type = 0
    private val prizeListViewModel: PrizeListFragmentViewModel by viewModel()
    private var nextId: Int = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_prize_list_item, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvPrizeList.adapter = adapter
        rcvPrizeList.addItemDecoration(VerticalSpaceItemDecoration(Utils.dpToPx(10f)))
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
        observeData()

        if (!areLecturesLoaded) {
            showDialog()
            prizeListViewModel.getPrizeList(type, nextId, Constants.PAGE_LIMITED)
            areLecturesLoaded = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
//        if (isFragmentUIActive && isVisibleToUser && !areLecturesLoaded) {
//            if (AppsterApplication.mAppPreferences.firstTypeOfBox != type) {
//                showDialog()
//                prizeListViewModel.getPrizeList(type, nextId, Constants.PAGE_LIMITED)
//                areLecturesLoaded = true
//            }
//        }
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(context)) {
            if (!isEnded) prizeListViewModel.getPrizeList(type, nextId, Constants.PAGE_LIMITED)
        }
    }

    private fun observeData() {

        prizeListViewModel.getError().observe(this, Observer {
            dismissDialog()
            rcvPrizeList.setLoading(false)
            it?.let {
                onErrorWebServiceCall(it.message, it.code)
            }
        })

        prizeListViewModel.getPrizeList.observe(this, Observer {
            adapter.updateItems(it?.listPrize)
            dismissDialog()
            nextId = it?.nextId!!
            isEnded = it.isEnded!!
            rcvPrizeList.setLoading(false)
            showNothingText()
        })
    }

    private fun showNothingText() {
        if (adapter.itemCount > 0) {
            tvNothing.visibility = View.GONE
        } else {
            tvNothing.visibility = View.VISIBLE
        }
    }

    private fun refreshData() {
        if (CheckNetwork.isNetworkAvailable(context)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            swiperefresh?.isRefreshing = false
            showDialog()
            prizeListViewModel.getPrizeList(type, nextId, Constants.PAGE_LIMITED)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                context
            )
        }
    }

    companion object {

        const val ARG_TYPE = "prize-type"
        @JvmStatic
        fun newInstance(type: Int) =
            PrizeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TYPE, type)
                }
            }
    }

    override fun onDisplayableItemClicked(v: View, item: DisplayableItem) {
        Timber.e("onDisplayableItemClicked %s", v.id)

        when (v.id) {
            R.id.tvViewDetail -> {
                if (item is Prize && item.webUrl != null) {
                    context?.run {
                        startActivity(
                            ActivityViewWeb.createIntent(
                                this,
                                item.webUrl,
                                true
                            )
                        )
                    }
                }
            }
            else -> Unit
        }
    }
}
