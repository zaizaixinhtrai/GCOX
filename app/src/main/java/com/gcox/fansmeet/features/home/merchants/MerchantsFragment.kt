package com.gcox.fansmeet.features.home.merchants

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
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.features.home.*
import com.gcox.fansmeet.features.profile.ChallengeListActivity
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.UiUtils
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class MerchantsFragment : BaseFragment(), HomeViewHolder.OnClickListener, HomeBannerItemViewHolder.OnClickListener,
    OnLoadMoreListenerRecyclerView {

    private var homeScreenAdapter: HomeScreenAdapter? = null
    private val homeViewModel: MerchantsViewModel by viewModel()
    private var typeFeed: Int? = 0
    private val listUser = arrayListOf<DisplayableItem>()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    companion object {
        const val CATEGORY_ID = "categoryId"
        @JvmStatic
        fun newInstance(categoryId: Int): MerchantsFragment {
            val f = MerchantsFragment()
            val b = Bundle()
            b.putInt(CATEGORY_ID, categoryId)
            f.arguments = b
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeFeed = arguments?.getInt(CATEGORY_ID)
        Timber.e("onCreate typeFeed %s", typeFeed)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.child_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        typeFeed = arguments?.getInt(CATEGORY_ID)
        observeData()
        homeScreenAdapter = HomeScreenAdapter(LiveShowDiffCallBack(), listUser, this, this)
        rcvListUser.adapter = homeScreenAdapter
        rcvListUser.setOnLoadMoreListener(this)
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
//        showDialog()
//        homeViewModel.getUsers(typeFeed!!, nextId)
        if (!areLecturesLoaded) {
            showDialog()
            homeViewModel.getUsers(HomeChildType.MERCHANTS, nextId)
            areLecturesLoaded = true
        }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        Timber.e("typeFeed %s", typeFeed)
//        if (isVisibleToUser && isResumed && !areLecturesLoaded) {
//            showDialog()
//            homeViewModel.getUsers(HomeChildType.MERCHANTS, nextId)
//            areLecturesLoaded = true
//        }
//    }

    override fun onFollowClicked(item: CelebritiesMode, position: Int) {
        if (!item.isFollow!!) homeViewModel.followUser(item.userId!!, position)
        else homeViewModel.unFollowUser(item.userId!!, position)
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
            homeViewModel.getUsers(HomeChildType.MERCHANTS, nextId)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                context
            )
        }
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(context?.applicationContext)) {
        }
    }

    private fun observeData() {
        homeViewModel.getError().observe(this, Observer {
            dismissDialog()
            rcvListUser.setLoading(false)
            swiperefresh?.isRefreshing = false
        })
        homeViewModel.getHomeData.observe(this, Observer {
            if (typeFeed == HomeChildType.MERCHANTS && nextId == Constants.WEB_SERVICE_START_PAGE) listUser.clear()
            listUser.addAll(it?.result!!)
            homeScreenAdapter?.notifyDataSetChanged()
            dismissDialog()
            rcvListUser.setLoading(false)
            swiperefresh?.isRefreshing = false
            nextId = it.nextId!!
            isEnded = it.isEnd!!
        })

        homeViewModel.followUse.observe(this, Observer {
            if (listUser.isNotEmpty() && listUser.size > it?.position!!) {
                if (listUser[it.position] is CelebritiesMode) {
                    val item = Objects.requireNonNull(listUser[it.position]) as CelebritiesMode
                    item.isFollow = true
                    listUser[it.position] = item
                    homeScreenAdapter?.notifyDataSetChanged()
                }
            }
        })

        homeViewModel.unFollowUse.observe(this, Observer {
            if (listUser.isNotEmpty() && listUser.size > it?.position!!) {
                if (listUser[it.position] is CelebritiesMode) {
                    val item = Objects.requireNonNull(listUser[it.position]) as CelebritiesMode
                    item.isFollow = false
                    listUser[it.position] = item
                    homeScreenAdapter?.notifyDataSetChanged()
                }
            }
        })
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