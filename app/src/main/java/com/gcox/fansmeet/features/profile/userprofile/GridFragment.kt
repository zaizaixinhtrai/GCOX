package com.gcox.fansmeet.features.profile.userprofile

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.core.fragment.BaseVisibleItemFragment
import com.gcox.fansmeet.customview.stickyheaders.PagedLoadScrollListener
import com.gcox.fansmeet.customview.stickyheaders.StickyHeaderLayoutManager
import com.gcox.fansmeet.features.challengedetail.ChallengeDetailActivity
import com.gcox.fansmeet.features.challengeentries.ChallengeEntriesActivity
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.GridModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.UserProfileGridViewModel
import com.gcox.fansmeet.models.UserModel
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.RecycleItemClickSupport
import com.gcox.fansmeet.util.UiUtils
import com.gcox.fansmeet.util.view.CustomScrollListener
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.ArrayList

import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.android.synthetic.main.fragment_me_grid_post.*

/**
 * Created by linh on 14/12/2016.
 */

class GridFragment : BaseVisibleItemFragment(), RecycleItemClickSupport.OnItemClickListener {

    private val TYPE_GRID = 1
    private val GRID_COLUMN_COUNT = 3

    internal var noDataView: TextView? = null

    internal var loadCompleteNotifier: PagedLoadScrollListener.LoadCompleteNotifier? = null
    private var gridAdapter: GirdViewUserPostAdapter? = null
    private val gridSpacingItemDecoration: UiUtils.GridSpacingItemDecoration? = null
    private var linearLayoutManager: StickyHeaderLayoutManager? = null
    private val userProfileView: UserProfileView? = null
    private val userProfileDetails: UserModel? = null

    private val nextIndexGrid = 0
    private val isLoading: Boolean = false
    private val isRefresh: Boolean = false

    private var userID: Int? = 0
    private var mUserName: String? = ""
    private val finishLoadData = false

    private val mIsAbleHandleRecyclerViewScrolling = AtomicBoolean(false)
    private val userProfileGridViewModel: UserProfileGridViewModel by viewModel()
    private val listItems = ArrayList<GridModel>()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userID = arguments!!.getInt(ListFragment.BUNDLE_USER_ID)
            mUserName = arguments!!.getString(ListFragment.BUNDLE_USER_NAME)
            if (mUserName == null) mUserName = ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me_grid_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isOwner()) tvNoData.text = getString(R.string.nothing_here)
        createAdapter()
        observeData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !areLecturesLoaded && userID != null) {
            showDialog()
            userProfileGridViewModel.getCelebrityGrid(userID!!, mUserName!!, nextId, false)
            areLecturesLoaded = true
        }
    }

    fun loadGrid(isRefresh: Boolean) {
        if (isRefresh) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            showDialog()
        }
        if (!isEnded) userProfileGridViewModel.getCelebrityGrid(userID!!, mUserName!!, nextId, isRefresh)
    }

    private fun isOwner(): Boolean {
        return userID != null && userID == AppsterApplication.mAppPreferences.userModel.userId
    }

    private fun observeData() {
        userProfileGridViewModel.getError().observe(this, Observer {
            dismissDialog()
            gridAdapter?.setLoaded()
        })

        userProfileGridViewModel.getCelebrityGrid.observe(this, Observer {
            if (it?.isRefresh!!) listItems.clear()
            listItems.addAll(it.result!!)
            gridAdapter?.notifyDataSetChanged()
            dismissDialog()
            nextId = it.nextId!!
            isEnded = it.isEnd!!
            gridAdapter?.setLoaded()
            checkIfNoData()
        })
    }

    private fun checkIfNoData() {
        if (listItems.size <= 0) {
            if (tvNoData != null) tvNoData.visibility = View.VISIBLE
        } else {
            if (tvNoData != null) tvNoData.visibility = View.GONE
        }
    }

    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View)  {
        if (!listItems.isNullOrEmpty() && 0 <= position && position < listItems.size) {
            val options =
                ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
            val item = listItems[position]
            if (item.postType == Constants.POST_TYPE_CHALLENGE) {
                val intent = ChallengeDetailActivity.newIntent(context!!, item.id!!, false)
                startActivityForResult(
                    intent,
                    Constants.REQUEST_CHALLENGE_DETAIL_ACTIVITY,
                    options.toBundle()
                )
            } else if (item.postType == Constants.CHALLENGE_SUBMISSION) {
                val intentGift = ChallengeEntriesActivity.newIntent(context!!, item.id!!)
                startActivityForResult(
                    intentGift,
                    Constants.REQUEST_CHALLENGE_ENTRIES_ACTIVITY,
                    options.toBundle()
                )
            }
        }
    }

    private fun createAdapter() {
        linearLayoutManager = StickyHeaderLayoutManager()
        gridAdapter = GirdViewUserPostAdapter(recyclerView, context, listItems, this)
        recyclerView!!.addItemDecoration(
            UiUtils.GridSpacingItemDecoration(
                GRID_COLUMN_COUNT,
                PixelUtil.dpToPx(context?.applicationContext, 5),
                false
            )
        )
        recyclerView!!.adapter = gridAdapter
        recyclerView!!.addOnScrollListener(CustomScrollListener(object : CustomScrollListener.ScrollListener {
            override fun onScrolling() {
            }

            override fun onSettling() {
            }

            override fun onNotScrolling() {
                mIsAbleHandleRecyclerViewScrolling.set(true)
            }

            override fun onScrolledDownward() {
                mIsAbleHandleRecyclerViewScrolling.set(false)
            }

            override fun onScrolledUp() {
                mIsAbleHandleRecyclerViewScrolling.set(true)
            }
        }))

        val manager = GridLayoutManager(context, GRID_COLUMN_COUNT)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return when (gridAdapter?.getItemViewType(position)) {
                    EndlessDelegateAdapter.LOAD_MORE -> manager.spanCount
                    else -> 1
                }
            }
        }
        recyclerView.layoutManager = manager

        gridAdapter?.setOnLoadMoreListener {
            if (CheckNetwork.isNetworkAvailable(context)) {
                loadGrid(false)
            }
        }
    }

    public fun updateUserId(id: Int) {
        this.userID = id
    }

    companion object {
        private const val BUNDLE_USER_ID = "user_id"
        private const val BUNDLE_USER_NAME = "BUNDLE_USER_NAME"

        @JvmStatic
        fun getInstance(userID: Int, userName: String): GridFragment {
            val f = GridFragment()
            val args = Bundle()
            args.putInt(BUNDLE_USER_ID, userID)
            args.putString(BUNDLE_USER_NAME, userName)
            f.arguments = args
            return f
        }
    }
}
