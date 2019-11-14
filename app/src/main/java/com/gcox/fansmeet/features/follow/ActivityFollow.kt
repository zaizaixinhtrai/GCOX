package com.gcox.fansmeet.features.follow

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.ButterKnife
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.FollowViewModel
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.UiUtils
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.reactivestreams.Subscription
import kotlinx.android.synthetic.main.followers.*
import java.util.*

/**
 * Created by User on 9/28/2015.
 */
class ActivityFollow : BaseToolBarActivity(), OnLoadMoreListenerRecyclerView, FollowListener {

    private var profileId: Int? = null
    private var type = TypeList.FOLLOWER

    //    private AdapterFollowUser adapter;
    private var arrFollowers: ArrayList<FollowItemModel>? = null

    private var adapterFollowTemp: FollowRecyclerAdapter? = null
    private val followViewModel: FollowViewModel by viewModel()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    enum class TypeList {
        FOLLOWER,
        FOLLOWING
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent?.extras
        if (bundle != null) {
            val temp = bundle.getInt(ConstantBundleKey.BUNDLE_TYPE_KEY)
            type = TypeList.values()[temp]
            profileId = bundle.getInt(ConstantBundleKey.BUNDLE_PROFILE_ID_KEY)
            setTopBarTile(if (type == TypeList.FOLLOWER) getString(R.string.profile_followers_title) else getString(R.string.profile_following_title))
        }
        observeData()
        utility = DialogInfoUtility()
        if (profileId != null) getData(true)

    }

    override fun getLayoutContentId(): Int {
        return R.layout.followers
    }

    override fun init() {
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { v -> onBackPressed() }
        handleTurnoffMenuSliding()
        arrFollowers = ArrayList()

        adapterFollowTemp = FollowRecyclerAdapter(this, recyclerView, arrFollowers, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(UiUtils.ListSpacingItemDecoration(PixelUtil.dpToPx(this, 5), false))
        recyclerView.adapter = adapterFollowTemp
        recyclerView.setOnLoadMoreListener(this)
        UiUtils.setColorSwipeRefreshLayout(swiperefresh!!)
        swiperefresh!!.setOnRefreshListener { this.refreshData() }
    }

    private fun observeData() {
        followViewModel.getError().observe(this, Observer {
            dismissDialog()
            handleError(it?.message, it?.code!!)
            swiperefresh?.isRefreshing = false
        })

        followViewModel.getFollowListResponse.observe(this, Observer {
            dismissDialog()
            progressDataReturn(it!!)
            swiperefresh?.isRefreshing = false
        })

        followViewModel.followUse.observe(this, Observer {
            if (it != null) {
                if (arrFollowers!!.isNotEmpty() && arrFollowers!!.size > it.position) {
                    arrFollowers!![it.position].isFollow = true
                    adapterFollowTemp?.notifyDataSetChanged()
                }
            }
        })

        followViewModel.unFollowUse.observe(this, Observer {
            if (it != null) {
                if (arrFollowers!!.isNotEmpty() && arrFollowers!!.size > it.position) {
                    arrFollowers!![it.position].isFollow = false
                    adapterFollowTemp?.notifyDataSetChanged()
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
        if (arrFollowers!!.isNotEmpty()) {
            for (i in 0 until arrFollowers!!.size) {
                val item = arrFollowers!![i]
                if (item.userId == userId) {
                    item.isFollow = followStatus
                    arrFollowers!![i] = item
                    adapterFollowTemp?.notifyDataSetChanged()
                    return
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun refreshData() {
        if (!CheckNetwork.isNetworkAvailable(this@ActivityFollow)) {
            return
        }

        nextId = Constants.WEB_SERVICE_START_PAGE
        getData(false)
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded) getData(false)
        }
    }

    private fun getData(isShowDialog: Boolean) {
        if (isShowDialog) showDialog(this, getString(R.string.connecting_msg))
        followViewModel.getFollowList(profileId!!, nextId, type.ordinal)
    }

    private fun progressDataReturn(followResponseModel: BaseDataPagingResponseModel<FollowItemModel>) {

        if (nextId == Constants.WEB_SERVICE_START_PAGE && !arrFollowers!!.isEmpty() && followResponseModel.result != null && !followResponseModel.result.isEmpty()) {
            arrFollowers!!.clear()
            arrFollowers!!.addAll(followResponseModel.result)
        } else if (followResponseModel.result != null && !followResponseModel.result.isEmpty()) {
            arrFollowers!!.addAll(followResponseModel.result)
        }
        nextId = followResponseModel.nextId
        isEnded = followResponseModel.isEnd

        if (txtEmpty != null) {
            if (arrFollowers!!.isEmpty()) {
                txtEmpty!!.visibility = View.VISIBLE
            } else {
                txtEmpty!!.visibility = View.INVISIBLE
            }
        }

        adapterFollowTemp?.notifyDataSetChanged()
    }

    override fun onFollowClicked(position: Int, userId: Int, isFollowing: Boolean) {
        if (isFollowing) {
            followViewModel.unFollowUser(userId, position)
        } else {
            followViewModel.followUser(userId, position)
        }
    }

    override fun onUserImageClicked(userId: Int, userName: String) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        ActivityCompat.startActivityForResult(
            this,
            UserProfileActivity.newIntent(applicationContext, userId, userName),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }
}
