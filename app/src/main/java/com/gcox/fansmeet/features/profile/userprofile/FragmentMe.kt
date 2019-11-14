package com.gcox.fansmeet.features.profile.userprofile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.fragment.BaseVisibleItemFragment
import com.gcox.fansmeet.customview.BioTextView
import com.gcox.fansmeet.features.follow.ActivityFollow
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.userprofile.gift.DialogSendGift
import com.gcox.fansmeet.features.topfans.TopFanActivity
import com.gcox.fansmeet.models.eventbus.EventBusRefreshFragment
import com.gcox.fansmeet.util.DialogUtil
import com.gcox.fansmeet.util.UiUtils
import kotlinx.android.synthetic.main.fragment_profile_page_activity_new.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by User on 9/24/2015.
 */
class FragmentMe : BaseVisibleItemFragment(), AppBarLayout.OnOffsetChangedListener,
    OnUserProfileChangeView,
    OnUserActionChange {

    private var userProfileView: UserProfileView? = null
    var userID: Int = 0
    private var mUserName: String? = ""
    private var isOwnerApp: Boolean = false
    private var isAppOwner = false
    private var finishLoadData = false
    private var isLoading: Boolean = false
    private var hasRefreshedFromActivityResult: Boolean = false
    private val mIsShouldShowList = AtomicBoolean(false)

    private val toolbarOffset = 0
    private var toolbarHeight: Int = 0
    private var isNewPost = false
    private var isChangeProfileImage = false

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var listFragment: ListFragment? = null
    private var gridFragment: GridFragment? = null
    private var sendGift: DialogSendGift? = null
    var followStatusBefore: Boolean? = null
    var isFollowStatusChanced = false
    private var needRefresh = AtomicBoolean(false)
    private var userProfileDetails: CelebrityProfileModel? = null

    private val userInfoViewModel: UserInfoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userID = arguments!!.getInt(BUNDEL_USER_ID)
            mUserName = arguments!!.getString(BUNDEL_USER_NAME, "")
            isOwnerApp = arguments!!.getBoolean(BUNDEL_IS_TAB)
            if (mUserName == null) mUserName = ""
        }
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile_page_activity_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UiUtils.setColorSwipeRefreshLayout(swipeRefreshLayout)
        swipeRefreshLayout!!.setOnRefreshListener {
            onRefreshData()
            swipeRefreshLayout!!.isRefreshing = false
        }
        observeData()
        addHeaderView()

        if (isOwnerApp) userInfoViewModel.getCelebrityProfile(userID, mUserName!!)

    }

    //region implemented methods ===================================================================


    private fun onGetUserProfileError(message: String, code: Int) {
        if (isFragmentUIActive) {
            swipeRefreshLayout!!.isRefreshing = false
            isLoading = false
            onErrorWebServiceCall(message, code)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Timber.e("Me_setUserVisibleHint")
        if (isVisibleToUser && !areLecturesLoaded) {
            userInfoViewModel.getCelebrityProfile(userID, mUserName!!)
            areLecturesLoaded = true
        }

        if (userVisibleHint) {
            if (listFragment != null) listFragment!!.refreshRecyclerView()

        } else {
            if (listFragment != null) listFragment!!.handleStopVideosResetPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        appBarLayout!!.addOnOffsetChangedListener(this)
        userProfileView?.updateInfo()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        swipeRefreshLayout!!.isEnabled = i == 0
    }

    override fun onPause() {
        super.onPause()
        appBarLayout!!.removeOnOffsetChangedListener(this)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(event: EventBusRefreshFragment) {
        if (areLecturesLoaded) onRefreshData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (listFragment != null) listFragment!!.setUserActionChange(null)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        hasRefreshedFromActivityResult = false

        if (resultCode == BaseToolBarActivity.RESULT_OK) {
            when (requestCode) {
                Constants.POST_REQUEST, Constants.POST_CHALLENGE_REQUEST -> {
                    areLecturesLoaded = true
                    onRefreshData()
                }
            }
        }
    }

    override fun onFollowingClicked() {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!.applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intent = Intent(activity, ActivityFollow::class.java)
        intent.putExtra(
            ConstantBundleKey.BUNDLE_TYPE_KEY,
            ActivityFollow.TypeList.FOLLOWING.ordinal
        )
        intent.putExtra(ConstantBundleKey.BUNDLE_PROFILE_ID_KEY, userID)
        startActivityForResult(intent, Constants.REQUEST_FOLLOW, options.toBundle())
    }

    override fun onFollowerClicked() {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!.applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intent = Intent(activity, ActivityFollow::class.java)
        intent.putExtra(ConstantBundleKey.BUNDLE_TYPE_KEY, ActivityFollow.TypeList.FOLLOWER.ordinal)
        intent.putExtra(ConstantBundleKey.BUNDLE_PROFILE_ID_KEY, userID)
        startActivityForResult(intent, Constants.REQUEST_FOLLOW, options.toBundle())
    }

    override fun onFollowClicked(isFollow: Boolean) {
        if (isFollow) {
            userInfoViewModel.followUser(userID)
        } else {
            DialogUtil.showConfirmUnFollowUser(
                context as Activity,
                userProfileDetails?.displayName
            ) { userInfoViewModel.unFollowUser(userID) }
        }
    }

    override fun onChangeToListView() {
        viewPager.setCurrentItem(TAB_LIST_INDEX, true)
    }

    override fun onChangeToGridView() {
        viewPager.setCurrentItem(TAB_GRID_INDEX, true)
    }

    override fun onChangeToGift() {
        sendGift = DialogSendGift(activity, userID, true)
        sendGift?.show()
    }

    override fun onViewMoreClick() {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intentGift = TopFanActivity.newIntent(context!!, userID)
        startActivityForResult(intentGift, Constants.REQUEST_TOPFAN_ACTIVITY, options.toBundle())
    }

    override fun onTopFansImageClicked(userId: Int, userName: String) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        startActivityForResult(
            UserProfileActivity.newIntent(context!!, userId, userName),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }

    override fun onFollowChange(isFollow: Boolean, userFollowerCount: Long?) {
        userProfileDetails?.isFollow = isFollow
        setUserModel()
        isFollowStatusChanced = if (isFollow) {
            !followStatusBefore!!
        } else {
            followStatusBefore!!
        }
        if (userFollowerCount != null) userProfileView!!.updateFollowStatus(
            isFollow,
            userFollowerCount
        )
    }

    override fun onBlockUserChange(isBolock: Boolean) {
        if (isBolock) {
            val intent = activity?.intent
            intent?.putExtra(UserProfileActivity.ARG_USER_BLOCKED, true)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun observeData() {

        userInfoViewModel.getError().observe(this, Observer {
            if (it?.code == Constants.RESPONSE_FROM_WEB_SERVICE_404) {
                // This user does not exist.
                val acti = activity as? UserProfileActivity
                acti?.run {
                    if (!isFinishing && !isDestroyed) {
                        showNoUserDataDialog(it.message ?: "")
                    }
                }
            }
            dismissDialog()
        })

        userInfoViewModel.getCelebrityProfile.observe(this, Observer {
            userProfileDetails = it
            userProfileView?.setUserProfileDetails(it)
            setUserModel()
            if (it?.userId != null) userID = it.userId!!
            userInfoViewModel.getTopFans(userID)
            followStatusBefore = it?.isFollow
            setupViewPager()
        })

        userInfoViewModel.getTopFans.observe(this, Observer {
            userProfileView?.setTopFans(it?.result)
        })

        userInfoViewModel.followUse.observe(this, Observer {
            userProfileView!!.updateFollowStatus(true, it?.userFollowerCount!!)
            isFollowStatusChanced = !followStatusBefore!!
            AppsterApplication.mAppPreferences.userModel.followingCount = it.meFollowingCount!!
        })

        userInfoViewModel.unFollowUse.observe(this, Observer {
            userProfileView!!.updateFollowStatus(false, it?.userFollowerCount!!)
            isFollowStatusChanced = followStatusBefore!!
            AppsterApplication.mAppPreferences.userModel.followingCount = it.meFollowingCount!!
        })
    }

    private fun addHeaderView() {
        userProfileView = UserProfileView(
            activity,
            isAppOwner(),
            object : BioTextView.DisallowTouchEventCallback {
                override fun requestDisallowTouchEvent(disallow: Boolean) {
                    swipeRefreshLayout.isEnabled = !disallow
                }
            })
        userProfileView!!.setOnTabChange(this)
        toolbarHeight = userProfileView!!.height
        headerUserInfoView!!.addView(userProfileView)
    }

    private fun onRefreshData() {
        userInfoViewModel.getCelebrityProfile(userID, mUserName!!)
        listFragment?.getListPost(true)
        gridFragment?.loadGrid(true)
//        if (TAB_LIST_INDEX == viewPager.currentItem) {
//            listFragment!!.getListPost(true)
//        } else {
//            gridFragment?.loadGrid(true)
//        }
    }


    private fun setupViewPager() {
        if (viewPagerAdapter != null) {
            return
        }
        viewPagerAdapter = ViewPagerAdapter()
        viewPager!!.adapter = viewPagerAdapter
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                userProfileView?.changeTabIcon(position)
                if (position == FragmentMe.TAB_GIFT_INDEX) {
                    appBarLayout!!.setExpanded(true, true)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun isAppOwner(): Boolean {
        return AppsterApplication.mAppPreferences.userModel.userId == userID
    }

    private fun setUserActionChange() {
        if (listFragment != null) listFragment!!.setUserActionChange(this)
    }

    private fun setUserModel() {
        if (listFragment != null && userProfileDetails != null) listFragment!!.setUserModel(
            userProfileDetails!!
        )
    }

    internal inner class ViewPagerAdapter : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                1 -> {
                    if (gridFragment == null) {
                        gridFragment = GridFragment.getInstance(userID, mUserName!!)
                    }
                    return gridFragment
                }

                0 -> {
                    if (listFragment == null) {
                        listFragment = ListFragment.getInstance(userID, mUserName!!, isOwnerApp)
                        setUserActionChange()
                        setUserModel()
                    }
                    return listFragment
                }
                else -> {
                    if (listFragment == null) {
                        listFragment = ListFragment.getInstance(userID, mUserName!!, isOwnerApp)
                    }
                    return listFragment
                }
            }
        }

        override fun getCount(): Int {
            return MAX_PAGE_COUNT_APP_OWNER
        }
    }

    companion object {

        const val BUNDEL_USER_NAME = "user_name"
        const val BUNDEL_USER_ID = "user_id"
        const val BUNDEL_IS_TAB = "user_tab"
        const val TAB_LIST_INDEX = 0
        const val TAB_GRID_INDEX = 1
        const val TAB_GIFT_INDEX = 2
        private const val MAX_PAGE_COUNT = 3
        private const val MAX_PAGE_COUNT_APP_OWNER = 2

        /**
         * Here we use [], which means that only one video playback is possible.
         */

        @JvmStatic
        fun getInstance(userID: Int, userName: String, isTab: Boolean): FragmentMe {
            val f = FragmentMe()
            val args = Bundle()
            args.putInt(BUNDEL_USER_ID, userID)
            args.putString(BUNDEL_USER_NAME, userName)
            args.putBoolean(BUNDEL_IS_TAB, isTab)
            f.arguments = args
            return f
        }
    }

}
