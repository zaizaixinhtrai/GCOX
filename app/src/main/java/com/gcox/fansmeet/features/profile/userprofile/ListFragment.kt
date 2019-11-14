package com.gcox.fansmeet.features.profile.userprofile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.dialog.DialogReport
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation
import com.gcox.fansmeet.core.dialog.SharePostDialog
import com.gcox.fansmeet.core.fragment.BaseVisibleItemFragment
import com.gcox.fansmeet.features.challengedetail.ChallengeDetailActivity
import com.gcox.fansmeet.features.comment.ActivityComments
import com.gcox.fansmeet.features.comment.CommentsType
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.postchallenge.PostChallengeActivity
import com.gcox.fansmeet.features.profile.ItemModelClassNewsFeed
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserPostViewHolder
import com.gcox.fansmeet.features.profile.userprofile.viewholders.UserChallengeViewHolder
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.manager.VideosManager
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.util.*
import com.gcox.fansmeet.util.view.CustomScrollListener
import kotlinx.android.synthetic.main.fragment_me_list_grid_post.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by ngoc on 14/12/2016.
 */

class ListFragment : BaseVisibleItemFragment(), UserChallengeViewHolder.OnClickListener,
    UserPostViewHolder.OnClickListener, OnLoadMoreListenerRecyclerView {

    internal var isLoading: Boolean = false
    private val isRefresh: Boolean = false

    private var userId: Int? = 0
    private var mUserName: String? = ""
    private var isOwner: Boolean = false

    private val isNewPost = false
    private val isChangeProfileImage = false
    private var currentVideosLink = ""
    /**
     * Here we use [], which means that only one video playback is possible.
     */
    //    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(metaData -> {
    //
    //    });
    private val mIsAbleHandleRecyclerViewScrolling = AtomicBoolean(false)
    private var adapter: UserProfileAdapter? = null
    private val listItems = ArrayList<DisplayableItem>()
    private val userProfileViewModel: UserProfileListViewModel by viewModel()
    private var mVideoPlayerManager = VideosManager.getInstance()
    internal var layoutManager: RecyclerView.LayoutManager? = null
    private var positionHelper: RecyclerViewPositionHelper? = null
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false
    private var itemSubmit: CelebrityModel? = null
    private val positionDelete = AtomicInteger(-1)
    private var onUserActionChange: OnUserActionChange? = null
    private var userProfileDetails: CelebrityProfileModel? = null
    private var isDownward = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            userId = arguments!!.getInt(BUNDLE_USER_ID)
            mUserName = arguments!!.getString(BUNDLE_USER_NAME)
            isOwner = arguments!!.getBoolean(BUNDLE_OWNER)
            if (mUserName == null) mUserName = ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me_list_grid_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        observeData()
        if (!isOwner()) tvNoData.text = getString(R.string.nothing_here)
        if (userId != null) {
            showDialog()
            userProfileViewModel.getCelebrityList(userId!!, mUserName!!, nextId, false)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            refreshRecyclerView()
        }
        Timber.e("setUserVisibleHint")
        if (isVisibleToUser && isFragmentUIActive) {
            playVideosAfterLoadData()
            Timber.e("ListFragment playVideosAfterLoadData")
        } else {
            handleStopVideosResetPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        playVideosAfterLoadData()
    }

    override fun onPause() {
        super.onPause()
        handleStopVideosResetPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handleStopVideosResetPlayer()
    }

    private fun isOwner(): Boolean {
        return userId != null && userId == AppsterApplication.mAppPreferences.userModel.userId
    }

    override fun canNotJoinChallenge(message: String) {
        Toast.makeText(context?.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onJoinClicked(item: CelebrityModel) {
        if (item.userId != AppsterApplication.mAppPreferences.userModel.userId) {
            itemSubmit = item
            showDialog()
            userProfileViewModel.checkCanSubmitChallenge(item.id!!)
        } else {
            Toast.makeText(
                context?.applicationContext,
                context?.getString(R.string.can_not_join_challenge),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onLikeClicked(item: CelebrityModel, position: Int) {
        var typeLike = LikeType.CHALLENGES
        if (item.postType == Constants.CHALLENGE_SUBMISSION) typeLike = LikeType.ENTRIES

        if (!item.isLike!!) userProfileViewModel.like(item.id!!, position, typeLike)
        else userProfileViewModel.unlike(item.id!!, position, typeLike)

    }

    override fun onViewDetailClicked(item: CelebrityModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
        val intentGift = ChallengeDetailActivity.newIntent(context!!, item.id!!, false)
        startActivityForResult(
            intentGift,
            Constants.REQUEST_CHALLENGE_DETAIL_ACTIVITY,
            options.toBundle()
        )
    }

    override fun onCommentClicked(item: CelebrityModel, position: Int) {
        var postType: Int = CommentsType.CHALLENGES
        if (item.postType == Constants.CHALLENGE_SUBMISSION) postType = CommentsType.ENTRIES
        val options =
            ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
        val intent =
            ActivityComments.newIntent(context!!, item.id!!, item.userId!!, postType, position)
        startActivityForResult(
            intent,
            Constants.COMMENT_REQUEST,
            options.toBundle()
        )
    }

    private fun onDeletePost(postId: Int, position: Int, postType: Int?) {
        if (postType == null) return

        if (!CheckNetwork.isNetworkAvailable(context)) {
            DialogInfoUtility.getInstance().showMessage(
                getString(R.string.app_name),
                resources.getString(
                    R.string.no_internet_connection
                ), context
            )
        } else {
            showDialog()
            positionDelete.set(position)
            userProfileViewModel.deleteChallenge(postId, postType!!)
        }
    }

    override fun onShareClicked(item: CelebrityModel, position: Int) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                SocialManager.getInstance().getShareContent(
                    context?.applicationContext,
                    item.userName,
                    "",
                    SocialManager.SHARE_TYPE_POST,
                    false
                ) + item.webPostUrl
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_via)))
    }

    private fun showChooseShareType(item: CelebrityModel) {

        val sharePostDialog = SharePostDialog.newInstance()
        sharePostDialog.setChooseShareListenner(object : SharePostDialog.ChooseShareListenner {
            override fun chooseShareFacebook() {
                SocialManager.getInstance().shareFeedToFacebook(context, item)
            }

            override fun chooseShareInstagram() {
                SocialManager.getInstance().shareFeedToInstagram(context, item)
            }

            override fun chooseShareTwtter() {
                SocialManager.getInstance().shareFeedToTwitter(context, item, false)
            }

            override fun chooseShareEmail() {
                SocialManager.getInstance().shareFeedToShareAction(true, context, item, false)
            }

            override fun chooseShareWhatApp() {
                SocialManager.getInstance().shareFeedToWhatsapp(context, item, false)
            }

            override fun copyLink() {
                CopyTextUtils.CopyClipboard(
                    getApplicationContext(),
                    item.webPostUrl,
                    getString(R.string.share_link_copied)
                )
            }

            override fun chooseShareOthers() {
                SocialManager.getInstance().shareFeedToShareAction(false, context, item, false)
            }
        })

        sharePostDialog.show(fragmentManager, "Share")
    }

    override fun onUserNameClicked(userId: Int?) {
        if (userId != null && userId == AppsterApplication.mAppPreferences.userModel.userId) return
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        startActivityForResult(
            UserProfileActivity.newIntent(context!!, userId!!, ""),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(context?.applicationContext)) {
            if (!isEnded) userProfileViewModel.getCelebrityList(userId!!, mUserName!!, nextId, false)
        }
    }

    override fun onOptionMenuClicked(item: CelebrityModel, position: Int) {
        val isAppOwner =
            AppsterApplication.mAppPreferences.userModel.userId == item.userId
        var isFollow = true
        var isReport = false
        if (userProfileDetails != null) isFollow = userProfileDetails!!.isFollow!!
        if (item.isReport != null) isReport = item.isReport!!

        if (isAppOwner) {
            CustomDialogUtils.showOwnerFeedOptionPopup(
                context!!,
                OnDialogMenuItemClickListener(position, item),
                getDeleteString(item.postType!!),
                getEditString(item.postType!!)
            )
        } else {
            CustomDialogUtils.showFeedOptionPopup(
                context!!,
                OnDialogMenuItemClickListener(position, item),
                getFollowMessage(isFollow),
                getReportMessage(isReport),
                context!!.getString(R.string.block_user)
            )
        }
    }

    fun setUserActionChange(listener: OnUserActionChange?) {
        onUserActionChange = listener
    }

    fun setUserModel(userModel: CelebrityProfileModel) {
        userProfileDetails = userModel
    }

    fun getListPost(isRefresh: Boolean) {
        if (isRefresh) {
            handleStopVideosResetPlayer()
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            showDialog()
        }
        if (!isEnded) userProfileViewModel.getCelebrityList(userId!!, mUserName!!, nextId, isRefresh)
    }

    private fun createAdapter() {

        adapter = UserProfileAdapter(null, listItems, this, this, mVideoPlayerManager)
        recyclerView!!.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(getApplicationContext())
        recyclerView.addOnScrollListener(CustomScrollListener(object : CustomScrollListener.ScrollListener {
            override fun onScrolling() {
            }

            override fun onSettling() {
            }

            override fun onNotScrolling() {
                mIsAbleHandleRecyclerViewScrolling.set(true)
            }

            override fun onScrolledDownward() {
                mIsAbleHandleRecyclerViewScrolling.set(false)
                isDownward = true
            }

            override fun onScrolledUp() {
                mIsAbleHandleRecyclerViewScrolling.set(true)
                isDownward = false
            }
        }))

        positionHelper = RecyclerViewPositionHelper.createHelper(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                handlePlayVideosItem(newState)
            }
        })
        recyclerView.setOnLoadMoreListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.COMMENT_REQUEST -> if (data != null) getComment(data)
                Constants.REQUEST_EDIT_CHALLENGE -> if (data != null) getChallengeData(data)
                Constants.REQUEST_EDIT_POST -> if (data != null) getChallengeData(data)
            }
        }
    }

    private fun getChallengeData(data: Intent) {
        val extras = data.extras ?: return
        var updateItem: CelebrityModel? = null
        updateItem = data.extras!!.getParcelable(PostChallengeActivity.CHALLENGE_MODEL)
        val position = data.extras!!.getInt(PostChallengeActivity.POSITION)
        var item: CelebrityModel? = null
        if (listItems.size > position && position >= 0) {
            item = Objects.requireNonNull(listItems[position]) as CelebrityModel
        }
        if (item != null && updateItem != null && item.id == updateItem.id) {
            listItems[position] = updateItem
            adapter!!.notifyItemChanged(position)
        }
    }

    private fun getComment(data: Intent) {

        val extras = data.extras ?: return
        val positionOnListview = extras.getInt(ConstantBundleKey.BUNDLE_COMMENT_POSITION)
        val postId = extras.getInt(ConstantBundleKey.BUNDLE_POST_ID_KEY)

        val item = Objects.requireNonNull(listItems[positionOnListview]) as CelebrityModel
        if (item.id == postId && (item.postType == Constants.USER_POST_NORMAL || item.postType == Constants.CHALLENGE_SUBMISSION)) {
            val arrCommentReturn: ArrayList<ItemClassComments>? =
                extras.getParcelableArrayList<ItemClassComments>(ConstantBundleKey.BUNDLE_LIST_COMMENT) ?: return
            val commentCounts = extras.getInt(ConstantBundleKey.BUNDLE_COMMENT_COUNT)
            item.comments = arrCommentReturn!!
            if (commentCounts >= 0) item.commentCount = commentCounts
            listItems[positionOnListview] = item
            adapter!!.notifyItemChanged(positionOnListview)
        }
    }

    private fun observeData() {
        userProfileViewModel.getError().observe(this, Observer {
            it?.let {
                (activity as BaseActivity?)?.handleError(it.message,it.code)
            }
            dismissDialog()
            recyclerView.setLoading(false)
        })

        userProfileViewModel.getCelebrity.observe(this, Observer {
            nextId = it?.nextId!!
            isEnded = it.isEnd!!
            if (it.isRefresh) listItems.clear()
            listItems.addAll(it.result!!)
            adapter?.notifyDataSetChanged()
            dismissDialog()
            playVideosAfterLoadData()
            recyclerView.setLoading(false)
            checkIfNoData()
        })

        userProfileViewModel.getLikeResponse.observe(this, Observer {
            if (listItems.isNotEmpty() && listItems.size > it?.position!!) {
                if (listItems[it.position!!] is CelebrityModel) {
                    val item = Objects.requireNonNull(listItems[it.position!!]) as CelebrityModel
                    if (item.id == it.postId!!) {
                        item.likeCount = it.likeCount
                        item.isLike = true
                        listItems[it.position!!] = item
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        userProfileViewModel.getUnlikeResponse.observe(this, Observer {
            if (listItems.isNotEmpty() && listItems.size > it?.position!!) {
                if (listItems[it.position!!] is CelebrityModel) {
                    val item = Objects.requireNonNull(listItems[it.position!!]) as CelebrityModel
                    if (item.id == it.postId!!) {
                        item.likeCount = it.likeCount
                        item.isLike = false
                        listItems[it.position!!] = item
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        })

        userProfileViewModel.canSubmitChallenge.observe(this, Observer {
            dismissDialog()
            if (it != null && it == true) {
                if (itemSubmit != null) (Objects.requireNonNull(activity) as UserProfileActivity).joinChallengeClicked(
                    itemSubmit!!
                )
            } else {
                canNotJoinChallenge(getString(R.string.toast_if_joined_challege))
            }
        })

        userProfileViewModel.deleteChallenge.observe(this, Observer {
            dismissDialog()
            val position = positionDelete.get()
            if (position >= 0 && position < listItems.size) {
                listItems.removeAt(position)
                adapter?.notifyDataSetChanged()
                positionDelete.set(-1)
            }
        })

        userProfileViewModel.followUse.observe(this, Observer {
            dismissDialog()
            AppsterApplication.mAppPreferences.userModel.followingCount = it?.meFollowingCount!!
            if (onUserActionChange != null && it.userFollowerCount != null) onUserActionChange!!.onFollowChange(
                true,
                it.userFollowerCount!!
            )
        })

        userProfileViewModel.unFollowUse.observe(this, Observer {
            dismissDialog()
            AppsterApplication.mAppPreferences.userModel.followingCount = it?.meFollowingCount!!
            if (onUserActionChange != null && it.userFollowerCount != null) onUserActionChange!!.onFollowChange(
                false,
                it.userFollowerCount!!
            )
        })

        userProfileViewModel.blockUser.observe(this, Observer {
            if (onUserActionChange != null) onUserActionChange!!.onBlockUserChange(true)
        })

        userProfileViewModel.reportPost.observe(this, Observer {
            it?.let {
                if (listItems.isNotEmpty() && listItems.size > it.position) {
                    if (listItems[it.position] is CelebrityModel) {
                        val item = Objects.requireNonNull(listItems[it.position]) as CelebrityModel
                        if (item.id == it.postId) {
                            item.isReport = it.type == Constants.REPORT_TYPE
                            listItems[it.position] = item
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }

                if (it.type == Constants.REPORT_TYPE)
                    Toast.makeText(
                        context?.applicationContext,
                        getString(R.string.report_user_text),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        })
    }

    private fun checkIfNoData() {
        if (listItems.size <= 0) {
            if (tvNoData != null) tvNoData.visibility = View.VISIBLE
        } else {
            if (tvNoData != null) tvNoData.visibility = View.GONE
        }
    }

    fun refreshRecyclerView() {
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
            playVideosAfterLoadData()
        }
    }

    private fun playVideosAfterLoadData() {
        if (!listItems.isEmpty()) {
            recyclerView.post { handlePlayVideosItem(RecyclerView.SCROLL_STATE_IDLE) }
        }
    }

    private fun handlePlayVideosItem(newState: Int) {

        var firstVisibleItemPosition = positionHelper!!.findFirstVisibleItemPosition()
        var lastVisibleItemPosition = positionHelper!!.findLastVisibleItemPosition()

        val lastVisibleItemPercents =
            getVisibilityPercents(recyclerView.findViewHolderForAdapterPosition(lastVisibleItemPosition)?.itemView)
        val firstVisibleItemPercents =
            getVisibilityPercents(recyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition)?.itemView)

        Timber.e("lastVisibleItemPercents %s", lastVisibleItemPercents)
        Timber.e("firstVisibleItemPercents %s", firstVisibleItemPercents)
        val currentPosition = if (firstVisibleItemPercents > lastVisibleItemPercents) {
            firstVisibleItemPosition
        } else {
            lastVisibleItemPosition
        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {

            Timber.e("currentPosition %s", currentPosition)
            if (recyclerView.findViewHolderForAdapterPosition(currentPosition) is UserPostViewHolder) {

                val itemViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(currentPosition) as UserPostViewHolder
                val currentVisibleItemPercents = getVisibilityPercents(itemViewHolder.itemView)
                Timber.e("Percents %s", currentVisibleItemPercents)

                val model = itemViewHolder.getPostData()
                if (!StringUtil.isNullOrEmptyString(model.video) && currentVisibleItemPercents > Constants.PERCENT_MIN_VISIBLE) {
                    if (!model.video.equals(currentVideosLink)) {
                        currentVideosLink = model.video!!
                        Timber.e("play video %s", currentVideosLink)
                        itemViewHolder.autoPlayVideo(model.video!!)
                    }
                } else {
                    handleStopVideosResetPlayer()
                }
            } else {
                val view = recyclerView.findViewHolderForAdapterPosition(currentPosition)
                if (view?.itemView != null && getVisibilityPercents(view.itemView) <= Constants.PERCENT_MAX_GONE) {
                    handleStopVideosResetPlayer()
                }
            }
        } else {
            val view = recyclerView.findViewHolderForAdapterPosition(currentPosition)
            if (view?.itemView != null && getVisibilityPercents(view.itemView) <= Constants.PERCENT_MAX_GONE) {
                handleStopVideosResetPlayer()
            }
        }
    }

    fun handleStopVideosResetPlayer() {
        currentVideosLink = ""
        mVideoPlayerManager.stopAnyPlayback()
    }

    private fun getDeleteString(typePost: Int): String {
        if (context == null) return ""
        return if (typePost == Constants.POST_TYPE_CHALLENGE) {
            context!!.getString(R.string.newsfeed_menu_del_challenge)
        } else if (typePost == Constants.USER_POST_NORMAL) {
            context!!.getString(R.string.newsfeed_menu_del_post)
        } else {
            context!!.getString(R.string.newsfeed_menu_del_submission)
        }
    }

    private fun getEditString(typePost: Int): String {
        if (context == null) return ""
        return if (typePost == Constants.POST_TYPE_CHALLENGE) {
            context!!.getString(R.string.newsfeed_menu_edit_challenge)
        } else if (typePost == Constants.USER_POST_NORMAL) {
            context!!.getString(R.string.newsfeed_menu_edit_post)
        } else {
            context!!.getString(R.string.newsfeed_menu_edit_submission)
        }
    }

    private fun getFollowMessage(isFollow: Boolean): String {
        return if (isFollow) context!!.getString(R.string.newsfeed_menu_unfolow) else context!!.getString(
            R.string.newsfeed_menu_folow
        )
    }

    private fun getConfirmDeleteString(typePost: Int): String {
        if (context == null) return ""
        return if (typePost == Constants.POST_TYPE_CHALLENGE) {
            context!!.getString(R.string.do_you_want_to_delete_this_challenge)
        } else {
            context!!.getString(R.string.do_you_want_to_delete_this_post)
        }
    }

    private fun getTitleDeleteString(typePost: Int): String {
        if (context == null) return ""
        return if (typePost == Constants.POST_TYPE_CHALLENGE) {
            context!!.getString(R.string.newsfeed_menu_del_challenge)
        } else {
            context!!.getString(R.string.newsfeed_menu_del_post)
        }
    }

    private fun getReportMessage(isReport: Boolean): String {
        return if (isReport) context!!.getString(R.string.newsfeed_menu_unrepost) else context!!.getString(R.string.newsfeed_menu_repost)
    }

    private fun onBlockUser(userId: Int) {
        userProfileViewModel.blockUser(userId)
    }

    internal fun handleFollowUnFollow(isFollow: Boolean, userID: Int, displayName: String) {
        if (isFollow) {
            DialogUtil.showConfirmUnFollowUser(context as Activity, displayName) { unFollowUser(userID) }
        } else {
            followUser(userID)
        }
    }

    private fun unFollowUser(userID: Int) {
        showDialog()
        userProfileViewModel.unFollowUser(userID)
    }

    private fun followUser(userID: Int) {
        showDialog()
        userProfileViewModel.followUser(userID)
    }

    internal fun handleReportClick(isReported: Boolean, challengeId: Int, position: Int) {
        if (isReported) {
            unReportPost(challengeId, position)
        } else {
            showDialogReportItem(challengeId, position)
        }
    }

    private fun showDialogReportItem(challengeId: Int, position: Int) {
        val dialogReport = DialogReport.newInstance()
        dialogReport.setChooseReportListenner { reason ->
            Timber.e("report reason %s", reason)
            reportPost(challengeId, reason, position)
        }
        dialogReport.show((context as BaseActivity).supportFragmentManager, "Report")
    }

    private fun reportPost(challengeId: Int, reason: String, position: Int) {
        userProfileViewModel.reportPost(challengeId, reason, Constants.REPORT_TYPE, position)
    }

    private fun unReportPost(challengeId: Int, position: Int) {
        userProfileViewModel.reportPost(challengeId, "", Constants.UNREPORT_TYPE, position)
    }

    private fun handleEdit(item: CelebrityModel, position: Int) {
        if (item.postType == Constants.POST_TYPE_CHALLENGE) {
            val intent = PostChallengeActivity.newIntent(context!!, true, item, position)
            val options =
                ActivityOptionsCompat.makeCustomAnimation(context!!, R.anim.push_in_to_right, R.anim.push_in_to_left)
            startActivityForResult(
                intent,
                Constants.REQUEST_EDIT_CHALLENGE,
                options.toBundle()
            )
        } else if (item.postType == Constants.USER_POST_NORMAL) {
            val intent = Intent(context!!, ActivityPostMedia::class.java)
            val options =
                ActivityOptionsCompat.makeCustomAnimation(
                    context!!.applicationContext,
                    R.anim.push_in_to_right,
                    R.anim.push_in_to_left
                )
            intent.putExtra(PostChallengeActivity.CHALLENGE_MODEL, item)
            intent.putExtra(PostChallengeActivity.POSITION, position)
            startActivityForResult(intent, Constants.REQUEST_EDIT_POST, options.toBundle())
        } else if (item.postType == Constants.CHALLENGE_SUBMISSION) {
            val intent = Intent(context!!, ActivityPostMedia::class.java)
            val options =
                ActivityOptionsCompat.makeCustomAnimation(
                    context!!.applicationContext,
                    R.anim.push_in_to_right,
                    R.anim.push_in_to_left
                )
            intent.putExtra(PostChallengeActivity.CHALLENGE_MODEL, item)
            intent.putExtra(PostChallengeActivity.POSITION, position)
            startActivityForResult(intent, Constants.REQUEST_EDIT_POST, options.toBundle())
        }
    }

    private inner class OnDialogMenuItemClickListener internal constructor(
        internal var position: Int,
        var item: CelebrityModel
    ) :
        CustomDialogUtils.FeedOptionCallback {

        override fun onOptionClicked(optionPos: Int) {
            var displayName = item.displayName
            val userId: Int = item.userId!!
            var isFollow = true
            if (userProfileDetails != null) isFollow = userProfileDetails!!.isFollow!!

            if (isMatchedUserId(userId)) {
                when (optionPos) {
                    0 /*Delete*/ -> DialogUtil.showConfirmDialog(
                        context as Activity,
                        getTitleDeleteString(item.postType!!),
                        getConfirmDeleteString(item.postType!!),
                        context?.getString(R.string.delete_comment_delete_button)
                    ) {
                        onDeletePost(item.id!!, position, item.postType)
                    }
                    1/*Edit*/ -> handleEdit(item, position)
                }
            } else {
                when (optionPos) {
                    0/*Follow/Unfollow*/ -> handleFollowUnFollow(isFollow, userId, displayName!!)
                    1/*Report/Unreport*/ -> {
                        if (listItems[position] is CelebrityModel) {
                            val item = Objects.requireNonNull(listItems[position]) as CelebrityModel
                            if (item.isReport != null) handleReportClick(item.isReport!!, item.id!!, position)
                        }
                    }

                    2/*Block*/ -> {
                        val userIdBlock = userId
                        val builder = DialogbeLiveConfirmation.Builder()
                        builder.title(context?.getString(R.string.block_this_user))
                            .message(context?.getString(R.string.block_confirmation_content))
                            .confirmText(context?.getString(R.string.string_block))
                            .onConfirmClicked { onBlockUser(userIdBlock) }
                            .build().show(context!!)
                    }
                }
            }
        }
    }

    private fun isMatchedUserId(userId: Int): Boolean {
        return AppsterApplication.mAppPreferences.userModel.userId == userId
    }

    companion object {
        const val BUNDLE_USER_ID = "user_id"
        const val BUNDLE_USER_NAME = "user_name"
        const val BUNDLE_OWNER = "owner"

        @JvmStatic
        fun getInstance(userID: Int, userName: String, isOwnerApp: Boolean): ListFragment {
            val f = ListFragment()
            val args = Bundle()
            args.putInt(BUNDLE_USER_ID, userID)
            args.putString(BUNDLE_USER_NAME, userName)
            args.putBoolean(BUNDLE_OWNER, isOwnerApp)
            f.arguments = args
            return f
        }
    }
}
