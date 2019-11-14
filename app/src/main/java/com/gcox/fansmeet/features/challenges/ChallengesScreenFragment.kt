package com.gcox.fansmeet.features.challenges

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.features.challengedetail.ChallengeDetailActivity
import com.gcox.fansmeet.features.challenges.viewholders.ChallengeViewHolder
import com.gcox.fansmeet.features.challengeentries.ChallengeEntriesActivity
import com.gcox.fansmeet.models.eventbus.EventBusRefreshEntries
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.UiUtils
import kotlinx.android.synthetic.main.fragment_challenges.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class ChallengesScreenFragment : BaseFragment(), ChallengeViewHolder.OnClickListener, OnLoadMoreListenerRecyclerView {

    private var adapter: ChallengeAdapter? = null
    private val challengesViewModel: ChallengesViewModel by viewModel()
    private var listEntries = mutableListOf<DisplayableItem>()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    companion object {
        @JvmStatic
        fun newInstance(): ChallengesScreenFragment {
            return ChallengesScreenFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        createAdapter()
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !areLecturesLoaded) {
            showDialog()
            challengesViewModel.getChallengeList(nextId, Constants.PAGE_LIMITED)
            areLecturesLoaded = true
        }
    }

    private fun refreshData() {
        swiperefresh?.isRefreshing = false
        if (CheckNetwork.isNetworkAvailable(context?.applicationContext)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            showDialog()
            challengesViewModel.getChallengeList(nextId, Constants.PAGE_LIMITED)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage("", activity!!.getString(R.string.no_internet_connection), activity)
        }

    }

    private fun createAdapter() {
        adapter = ChallengeAdapter(ChallengesDiffCallBack(), ArrayList(), this)
        rcvChallenges.adapter = adapter
        rcvChallenges.setOnLoadMoreListener(this)
    }

    override fun onUserImageClicked(item: Entries) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
        val intentGift = ChallengeEntriesActivity.newIntent(context!!, item.id!!)
        startActivityForResult(
            intentGift,
            Constants.REQUEST_CHALLENGE_ENTRIES_ACTIVITY,
            options.toBundle()
        )
    }

    override fun onViewEntriesClicked(item: ChallengeModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
        val intentGift = ChallengeDetailActivity.newIntent(context!!, item.id!!, true)
        startActivityForResult(
            intentGift,
            Constants.REQUEST_CHALLENGE_DETAIL_ACTIVITY,
            options.toBundle()
        )
    }

    private fun observeData() {
        challengesViewModel.getError().observe(this, Observer {
            dismissDialog()
            rcvChallenges.setLoading(false)
            swiperefresh?.isRefreshing = false
        })

        challengesViewModel.getChallengeListEntries.observe(this, Observer {
            if (it?.result?.isEmpty()!!) {
                isEnded = it.isEnd
                nextId = it.nextId
            } else {
                if (nextId == Constants.WEB_SERVICE_START_PAGE) listEntries.clear()
                listEntries.addAll(it.result!!)
                adapter?.updateItems(listEntries)
                isEnded = it.isEnd
                nextId = it.nextId
            }
            swiperefresh?.isRefreshing = false
            dismissDialog()
            rcvChallenges.setLoading(false)
        })
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(context?.applicationContext)) {
            if (!isEnded) challengesViewModel.getChallengeList(nextId, Constants.PAGE_LIMITED)
        }
    }

    fun setupList(): List<DisplayableItem> {
        var listUser = arrayListOf<DisplayableItem>()
        for (i in 1..10) {
//            listUser.add(ChallengeModel())
        }

        return listUser
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(event: EventBusRefreshEntries) {
        if (event.isRefresh) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            challengesViewModel.getChallengeList(nextId, Constants.PAGE_LIMITED)
        }
    }
}