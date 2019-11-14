package com.gcox.fansmeet.features.topfans

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.UiUtils

import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_toppan.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DecimalFormat

/**
 * Created by User on 8/31/2016.
 */
class TopFanActivity : BaseToolBarActivity(), TopFansAdapter.TopFansListener, OnLoadMoreListenerRecyclerView {

    private val watchers: ArrayList<TopFanModel>? = arrayListOf()

    private var adapter: TopFansAdapter? = null
    private var mTopFanModels: ArrayList<TopFanModel>? = arrayListOf()
    private val topFansViewModel: TopFansViewModel by viewModel()
    private var userId: Int? = 0
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    companion object {
        @JvmStatic
        fun newIntent(context: Context, userId: Int): Intent {
            val intent = Intent(context, TopFanActivity::class.java)
            intent.putExtra(Constants.USER_PROFILE_ID, userId)
            return intent
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent?.extras?.getInt(Constants.USER_PROFILE_ID)
        observeData()
        if (userId != null) {
            showDialog(this, getString(R.string.connecting_msg))
            topFansViewModel.getTopFans(userId!!, nextId, Constants.PAGE_LIMITED)
        }

    }

    private fun observeData() {
        topFansViewModel.getError().observe(this, Observer {
            dismissDialog()
            swiperefresh?.isRefreshing = false
            handleError(it?.message, it?.code!!)
        })

        topFansViewModel.getTopFans.observe(this, Observer {
            if (nextId == Constants.WEB_SERVICE_START_PAGE) mTopFanModels?.clear()
            mTopFanModels?.addAll(it?.responseModel?.result!!)
            adapter?.notifyDataSetChanged()
            dismissDialog()
            swiperefresh?.isRefreshing = false
            setTopFans(it?.responseModel?.result)
            it?.totalGiftCount?.let {
                val formatter = DecimalFormat("#,###")
                totalStars!!.text = formatter.format(it).replace(".", ",")
            }
            if (it?.responseModel?.nextId != null) nextId = it.responseModel?.nextId!!
            if (it?.responseModel?.isEnd != null) isEnded = it.responseModel?.isEnd!!
        })

    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded) topFansViewModel.getTopFans(userId!!, nextId, Constants.PAGE_LIMITED)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun initAdapter() {
        adapter = TopFansAdapter(this, recycleView, mTopFanModels, this)
        recycleView!!.layoutManager = LinearLayoutManager(this)
        recycleView!!.addItemDecoration(
            UiUtils.ListSpacingItemDecoration(
                PixelUtil.dpToPx(applicationContext, 4),
                false
            )
        )
        recycleView!!.adapter = adapter
        recycleView.setOnLoadMoreListener(this)

    }

    override fun onUserImageClickedListener(item: TopFanModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        ActivityCompat.startActivityForResult(
            this, UserProfileActivity.newIntent(applicationContext, item.userId, item.userName),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }

    private fun setTopFans(topFanModels: List<TopFanModel>?) {

        if (topFanModels == null) {
            return
        }

        when {
            topFanModels.isEmpty() -> {
                userImage1.loadImg(R.drawable.user_image_default)
                userImage2.loadImg(R.drawable.user_image_default)
                userImage3.loadImg(R.drawable.user_image_default)

            }
            topFanModels.size == 1 -> {
                userImage1.loadImg(topFanModels[0].userImage)
                userImage2.loadImg(R.drawable.user_image_default)
                userImage3.loadImg(R.drawable.user_image_default)
                userImage1.setOnClickListener { onUserImageClickedListener(topFanModels[0]) }

            }
            topFanModels.size == 2 -> {
                userImage1.loadImg(topFanModels[0].userImage)
                userImage2.loadImg(topFanModels[1].userImage)
                userImage3.loadImg(R.drawable.user_image_default)
                userImage1.setOnClickListener { onUserImageClickedListener(topFanModels[0]) }
                userImage2.setOnClickListener { onUserImageClickedListener(topFanModels[1]) }

            }
            topFanModels.size >= 3 -> {
                userImage1.loadImg(topFanModels[0].userImage)
                userImage2.loadImg(topFanModels[1].userImage)
                userImage3.loadImg(topFanModels[2].userImage)
                userImage1.setOnClickListener { onUserImageClickedListener(topFanModels[0]) }
                userImage2.setOnClickListener { onUserImageClickedListener(topFanModels[1]) }
                userImage3.setOnClickListener { onUserImageClickedListener(topFanModels[2]) }
            }
        }
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_toppan
    }

    override fun onResume() {
        super.onResume()

    }

    override fun init() {
        handleTurnoffMenuSliding()
        setTopBarTile(getString(R.string.toppan_title))
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        initAdapter()
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
    }

    private fun refreshData() {
        if (CheckNetwork.isNetworkAvailable(applicationContext)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            topFansViewModel.getTopFans(userId!!, nextId, Constants.PAGE_LIMITED)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this
            )
        }
    }
}
