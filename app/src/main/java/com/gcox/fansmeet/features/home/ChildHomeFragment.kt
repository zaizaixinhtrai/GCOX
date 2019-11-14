package com.gcox.fansmeet.features.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.features.home.viewholders.HomeBannerItemViewHolder
import com.gcox.fansmeet.features.home.viewholders.HomeViewHolder
import kotlinx.android.synthetic.main.child_fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.annotation.Nullable
import android.support.v4.app.ActivityOptionsCompat
import android.util.Log
import com.appster.extensions.inflate
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.features.profile.ChallengeListActivity
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.models.eventbus.EventBusRefreshFragment
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DialogUtil
import com.gcox.fansmeet.util.UiUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*

class ChildHomeFragment : BaseFragment(), HomeViewHolder.OnClickListener, HomeBannerItemViewHolder.OnClickListener,
    OnLoadMoreListenerRecyclerView {

    private var homeScreenAdapter: HomeScreenAdapter? = null
    private val homeViewModel: HomeViewModel by viewModel()
    private var typeFeed: Int? = 0
    private val listUser = arrayListOf<DisplayableItem>()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false
    private var tvComingSoonVisibility: Int = View.GONE

    companion object {
        const val COMING_SOON_VISIBILITY = "tvComingSoonVisibility"
        const val CATEGORY_ID = "categoryId"
        @JvmStatic
        fun newInstance(categoryId: Int): ChildHomeFragment {
            val f = ChildHomeFragment()
            val b = Bundle()
            b.putInt(CATEGORY_ID, categoryId)
            f.arguments = b
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeFeed = arguments?.getInt(CATEGORY_ID)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.child_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!areLecturesLoaded) observeData()
        homeScreenAdapter = HomeScreenAdapter(LiveShowDiffCallBack(), listUser, this, this)
        rcvListUser.adapter = homeScreenAdapter
        rcvListUser.setOnLoadMoreListener(this)
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.e("onDestroyView $this")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COMING_SOON_VISIBILITY, tvComingSoonVisibility)
        Timber.e("tvComingSoonVisibility %s", tvComingSoonVisibility)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (tvComingSoon != null) {
            tvComingSoon.visibility = tvComingSoonVisibility
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(event: EventBusRefreshFragment) {
        refreshData()
    }

    private fun getData() {
        if (!areLecturesLoaded) {
//            when (typeFeed) {
//                HomeChildType.CELEBRITIES, HomeChildType.EVENTS -> {
//                    showDialog()
//                    homeViewModel.getBanners()
//                }
//
//                HomeChildType.MERCHANTS -> {
//                    showDialog()
////                    homeViewModel.getUsers(typeFeed!!, nextId)
//                    homeViewModel.getBanners()
//                }
//            }
            showDialog()
            homeViewModel.getBanners()
            areLecturesLoaded = true
        }
    }

    override fun onFollowClicked(item: CelebritiesMode, position: Int) {
        if (!item.isFollow!!) homeViewModel.followUser(item.userId!!, position)
        else {
            DialogUtil.showConfirmUnFollowUser(
                context as Activity,
                item.displayName
            ) { homeViewModel.unFollowUser(item.userId!!, position) }

        }
    }

    override fun onBannerItemClicked(eventModel: HomeCurrentEventModel) {
    }

    override fun onUserImageClicked(item: CelebritiesMode) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
        startActivityForResult(
            UserProfileActivity.newIntent(context!!, item.userId!!, item.userName!!),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }

    private fun refreshData() {
        if (CheckNetwork.isNetworkAvailable(context?.applicationContext)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            swiperefresh?.isRefreshing = false
            showDialog()
            homeViewModel.getBanners()
//            if (typeFeed == HomeChildType.CELEBRITIES) {
//                homeViewModel.getBanners()
//            } else {
//                homeViewModel.getUsers(typeFeed!!, nextId)
//            }
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                context
            )
            swiperefresh?.isRefreshing = false
        }
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(context)) {
            if (!isEnded) homeViewModel.getUsers(typeFeed!!, nextId)
        }
    }

    private fun observeData() {
        homeViewModel.getError().observe(this, Observer {
            dismissDialog()
            rcvListUser.setLoading(false)
            swiperefresh?.isRefreshing = false
        })
        homeViewModel.getHomeData.observe(this, Observer {
            if (it != null) {
                Timber.e("getHomeData %s", it)
                listUser.addAll(it.result!!)
                homeScreenAdapter?.notifyDataSetChanged()
                dismissDialog()
                rcvListUser.setLoading(false)
                swiperefresh?.isRefreshing = false
                if (nextId == Constants.WEB_SERVICE_START_PAGE) checkEmptyList(it.result)
                nextId = it.nextId!!
                isEnded = it.isEnd!!
            }
        })

        homeViewModel.getBanners.observe(this, Observer {
            if (it != null) {
                Timber.e("getBanners %s", it)
                listUser.clear()
                val banner = BannerModel()
                banner.bannerItems = it
                listUser.add(banner)
                homeScreenAdapter?.notifyDataSetChanged()
                homeViewModel.getUsers(typeFeed!!, nextId)
                rcvListUser.setLoading(false)
            }
        })

        homeViewModel.followUse.observe(this, Observer {
            if (it != null) {
                if (listUser.isNotEmpty() && listUser.size > it?.position!!) {
                    if (listUser[it.position] is CelebritiesMode) {
                        val item = Objects.requireNonNull(listUser[it.position]) as CelebritiesMode
                        item.isFollow = true
                        listUser[it.position] = item
                        homeScreenAdapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        homeViewModel.unFollowUse.observe(this, Observer {
            if (it != null) {
                if (listUser.isNotEmpty() && listUser.size > it?.position!!) {
                    if (listUser[it.position] is CelebritiesMode) {
                        val item = Objects.requireNonNull(listUser[it.position]) as CelebritiesMode
                        item.isFollow = false
                        listUser[it.position] = item
                        homeScreenAdapter?.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun checkEmptyList(listUser: List<DisplayableItem>?) {
        if (listUser.isNullOrEmpty()) {
            tvComingSoon.visibility = View.VISIBLE
            tvComingSoonVisibility = View.VISIBLE
        } else {
            tvComingSoon.visibility = View.GONE
            tvComingSoonVisibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CODE_VIEW_USER_PROFILE -> {
                    if (data != null) {
                        val userId = data.getIntExtra(ConstantBundleKey.BUNDLE_USER_ID, 0)
                        val followStatus =
                            data.getBooleanExtra(ConstantBundleKey.BUNDLE_DATA_FOLLOW_USER_FROM_PROFILE_ACTIVITY, false)
                        changeFollowStatus(userId, followStatus)
                        val isBlock = data.getBooleanExtra(UserProfileActivity.ARG_USER_BLOCKED, false)
                        if (isBlock) {
//                            refreshData()
                            EventBus.getDefault().post(EventBusRefreshFragment())
                        }
                    }
                }
            }
        }
    }

    private fun changeFollowStatus(userId: Int, followStatus: Boolean) {
        if (listUser.isNotEmpty()) {
            for (i in 0 until listUser.size) {
                if (listUser[i] is CelebritiesMode) {
                    val item = Objects.requireNonNull(listUser[i]) as CelebritiesMode
                    if (item.userId == userId) {
                        item.isFollow = followStatus
                        listUser[i] = item
                        homeScreenAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
        }
    }

}