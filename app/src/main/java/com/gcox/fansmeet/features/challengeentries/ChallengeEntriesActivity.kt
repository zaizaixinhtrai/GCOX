package com.gcox.fansmeet.features.challengeentries

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.Typeface
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import com.appster.extensions.decodeEmoji
import com.appster.extensions.loadImg
import com.appster.extensions.toHashTag
import com.appster.extensions.toUserName
import com.facebook.FacebookSdk
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.dialog.DialogReport
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation
import com.gcox.fansmeet.core.dialog.SharePostDialog
import com.gcox.fansmeet.customview.CustomTypefaceSpan
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkUtil
import com.gcox.fansmeet.customview.autolinktextview.TouchableSpan
import com.gcox.fansmeet.features.comment.ActivityComments
import com.gcox.fansmeet.features.comment.CommentsType
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.features.profile.userprofile.gift.DialogSendGift
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.models.eventbus.EventBusRefreshEntries
import com.gcox.fansmeet.util.*
import com.gcox.fansmeet.util.view.ExpandableTextView
import com.ksyun.media.player.IMediaPlayer
import com.ksyun.media.player.KSYMediaPlayer
import kotlinx.android.synthetic.main.activity_contestant_screen.*
import kotlinx.android.synthetic.main.action_layout_entries.*
import kotlinx.android.synthetic.main.activity_base_tool_bar.*
import kotlinx.android.synthetic.main.feed_header_view.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.IOException
import java.util.*

class ChallengeEntriesActivity : BaseToolBarActivity() {
    private val challengeEntriesViewModel: ChallengeEntriesViewModel by viewModel()
    private var entriesId: Int? = 0
    private var model: EntriesModel? = null
    private var mKsyMediaPlayer: KSYMediaPlayer? = null
    private var mSurface: Surface? = null
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var isPause = false

    companion object {
        @JvmStatic
        fun newIntent(context: Context, challengeId: Int): Intent {
            val intent = Intent(context, ChallengeEntriesActivity::class.java)
            intent.putExtra(Constants.ENTRIES_ID, challengeId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleTurnoffMenuSliding()
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        setTopBarTile(getString(R.string.entries_title))
        observeData()

        entriesId = intent?.extras?.getInt(Constants.ENTRIES_ID)
        if (entriesId != null) {
            showDialog(this, getString(R.string.connecting_msg))
            challengeEntriesViewModel.viewContestantEntries(entriesId!!)
            challengeEntriesViewModel.getCommentsList(entriesId!!, CommentsType.ENTRIES)
        }
    }

    override fun onPause() {
        super.onPause()

        if (mKsyMediaPlayer != null) {
            stopVideo()
        }
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        if (isPause && model != null && model?.mediaType == Constants.POST_TYPE_VIDEO && !model!!.video.isNullOrEmpty()) {
            initVideoView(model!!.video!!)
        }
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVideo()
        releaseSurface()
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_contestant_screen
    }

    override fun init() {
        moreOption.visibility = View.GONE
        ivLike.setOnClickListener {
            if (!model?.isLike!!) challengeEntriesViewModel.like(model?.id!!)
            else challengeEntriesViewModel.unlike(model?.id!!)
        }
        UiUtils.setColorSwipeRefreshLayout(swiperefresh!!)
        swiperefresh!!.setOnRefreshListener { this.refreshData() }
        imgShare.setOnClickListener { onShareClicked(model) }
    }

    private fun refreshData() {
        if (!CheckNetwork.isNetworkAvailable(this)) {
            return
        }

        if (entriesId != null) {
            swiperefresh?.isRefreshing = false
            showDialog(this, getString(R.string.connecting_msg))
            challengeEntriesViewModel.viewContestantEntries(entriesId!!)
            challengeEntriesViewModel.getCommentsList(entriesId!!, CommentsType.ENTRIES)
        }
    }

    private fun observeData() {
        challengeEntriesViewModel.getError().observe(this, Observer {
            dismissDialog()
            swiperefresh?.isRefreshing = false
        })

        challengeEntriesViewModel.viewContestantEntries.observe(this, Observer {
            model = it
            setData(it!!)
            dismissDialog()
            swiperefresh?.isRefreshing = false
        })

        challengeEntriesViewModel.getLikeResponse.observe(this, Observer {
            model?.isLike = true
            setLikeImage()
            bindLikeCounts(it!!)
        })

        challengeEntriesViewModel.getUnlikeResponse.observe(this, Observer {
            model?.isLike = false
            setLikeImage()
            bindLikeCounts(it!!)
        })

        challengeEntriesViewModel.getCommentsListResponse.observe(this, Observer {
            bindCommentList(it?.result!!)
            swiperefresh?.isRefreshing = false
        })

        challengeEntriesViewModel.blockUser.observe(this, Observer {
            if (it != null && it) finish()
        })

        challengeEntriesViewModel.followUse.observe(this, Observer {
            AppsterApplication.mAppPreferences.userModel.followingCount = it?.meFollowingCount!!
            model?.isFollow = true
        })

        challengeEntriesViewModel.unFollowUse.observe(this, Observer {
            AppsterApplication.mAppPreferences.userModel.followingCount = it?.meFollowingCount!!
            model?.isFollow = false
        })

        challengeEntriesViewModel.reportEntries.observe(this, Observer {
            it?.let {
                if (model != null) {
                    model!!.isReport = it.type == Constants.REPORT_TYPE
                }
                if (it.type == Constants.REPORT_TYPE)
                    Toast.makeText(applicationContext, getString(R.string.report_user_text), Toast.LENGTH_SHORT).show()
            }
        })

        challengeEntriesViewModel.deleteEntries.observe(this, Observer {
            it?.let {
                if (it) {
                    EventBus.getDefault().post(EventBusRefreshEntries(true))
                    finish()
                }
            }
        })
    }

    private fun onShareClicked(item: EntriesModel?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                SocialManager.getInstance().getShareContent(
                    applicationContext,
                    item?.userName,
                    "",
                    SocialManager.SHARE_TYPE_POST,
                    false
                ) + item?.webPostUrl
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_via)))
    }

    private fun showChooseShareType(item: EntriesModel?) {

        if (item == null) return

        val modelShare = CelebrityModel()
        modelShare.image = item.image
        modelShare.video = item.video
        modelShare.webPostUrl = item.webPostUrl
        modelShare.mediaType = item.mediaType
        modelShare.description = item.caption
        modelShare.displayName = item.displayName
        modelShare.userName = item.userName

        val sharePostDialog = SharePostDialog.newInstance()
        sharePostDialog.setChooseShareListenner(object : SharePostDialog.ChooseShareListenner {
            override fun chooseShareFacebook() {
                SocialManager.getInstance().shareFeedToFacebook(mActivity, modelShare)
            }

            override fun chooseShareInstagram() {
                SocialManager.getInstance().shareFeedToInstagram(mActivity, modelShare)
            }

            override fun chooseShareTwtter() {
                SocialManager.getInstance().shareFeedToTwitter(mActivity, modelShare, false)
            }

            override fun chooseShareEmail() {
                SocialManager.getInstance().shareFeedToShareAction(true, mActivity, modelShare, false)
            }

            override fun chooseShareWhatApp() {
                SocialManager.getInstance().shareFeedToWhatsapp(mActivity, modelShare, false)
            }

            override fun copyLink() {
                CopyTextUtils.CopyClipboard(
                    FacebookSdk.getApplicationContext(),
                    item.webPostUrl,
                    getString(R.string.share_link_copied)
                )
            }

            override fun chooseShareOthers() {
                SocialManager.getInstance().shareFeedToShareAction(false, applicationContext, modelShare, false)
            }
        })

        sharePostDialog.show(supportFragmentManager, "Share")
    }

    private fun setData(model: EntriesModel) {
        useImage.loadImg(model.userImage)
        if (model.mediaType == Constants.POST_TYPE_QUOTES) {
            imageEntries.loadImg(model.userImage)
        } else {
            imageEntries.loadImg(model.image)
        }
        tvUserName.text = model.displayName
        bindContentPost(model.caption)
        setLikeImage()

        if (model.userId == AppsterApplication.mAppPreferences.userModel.userId) {
            btSendGift.visibility = View.GONE
        } else {
            btSendGift.setOnClickListener {
                val sendGift = DialogSendGift(this, model.id!!, false)
                sendGift.show()
            }
        }
        bindLikeCounts(model.likeCount!!)
        //comment section
        setViewMoreView(model.commentCount!!)
        tvTimer.text = SetDateTime.partTimeForFeedItem(model.created, applicationContext)
        imgComment.setOnClickListener { showComments(model) }
        tvMoreComments.setOnClickListener { imgComment.performClick() }
        if (!model.tagUsers.isNullOrEmpty()) {
            tvHashTag.visibility = View.VISIBLE
            tvHashTag.text = model.tagUsers.toHashTag()
        } else {
            tvHashTag.visibility = View.GONE
        }

        btMoreOption.setOnClickListener { onOptionMenuClicked(model) }

        if (model.mediaType == Constants.POST_TYPE_VIDEO && !model.video.isNullOrEmpty()) initVideoView(model.video!!)
    }

    private fun isAppOwner(): Boolean {
        return AppsterApplication.mAppPreferences.userModel.userId == model?.userId
    }

    private fun onOptionMenuClicked(item: EntriesModel) {

        var isFollow = true
        var isReport = false
        if (item.isFollow != null) isFollow = item.isFollow!!
        if (item.isReport != null) isReport = item.isReport!!

        if (isAppOwner()) {
            CustomDialogUtils.showOwnerFeedOptionPopup(
                this,
                OnDialogMenuItemClickListener(this, item),
                getString(R.string.newsfeed_menu_delete_submission),
                getString(R.string.newsfeed_menu_edit_submission)
            )
        } else {
            CustomDialogUtils.showFeedOptionPopup(
                this,
                OnDialogMenuItemClickListener(this, item),
                getFollowMessage(isFollow),
                getReportMessage(isReport),
                getString(R.string.block_user)
            )
        }
    }

    private fun getFollowMessage(isFollow: Boolean): String {
        return if (isFollow) getString(R.string.newsfeed_menu_unfolow) else getString(
            R.string.newsfeed_menu_folow
        )
    }

    private fun getReportMessage(isReport: Boolean): String {
        return if (isReport) getString(R.string.newsfeed_menu_unrepost) else getString(R.string.newsfeed_menu_repost)
    }

    private inner class OnDialogMenuItemClickListener internal constructor(
        val context: Context,
        var item: EntriesModel
    ) :
        CustomDialogUtils.FeedOptionCallback {

        override fun onOptionClicked(optionPos: Int) {
            var displayName = item.displayName
            val userId: Int = item.userId!!
            var isFollow = true
            var isReported: Boolean? = false
            if (item.isFollow != null) isFollow = item.isFollow!!
            if (item.isReport != null) isReported = item.isReport

            if (isAppOwner()) {
                when (optionPos) {
                    0 /*Delete*/ -> DialogUtil.showConfirmDialog(
                        context as Activity,
                        getString(R.string.newsfeed_menu_delete_submission),
                        getString(R.string.do_you_want_to_delete_this_submisson),
                        getString(R.string.delete_comment_delete_button)
                    ) {
                        onDeleteSubmission(item.id!!)
                    }
                    1/*Edit*/ -> handleEdit(item)
                }
            } else {
                when (optionPos) {
                    0/*Follow/Unfollow*/ -> handleFollowUnFollow(isFollow, userId, displayName!!)

                    1/*Report/Unreport*/ -> handleReportClick(isReported!!, item.id!!, 0)

                    2/*Block*/ -> {
                        val userIdBlock = userId
                        val builder = DialogbeLiveConfirmation.Builder()
                        builder.title(context.getString(R.string.block_this_user))
                            .message(context.getString(R.string.block_confirmation_content))
                            .confirmText(context.getString(R.string.string_block))
                            .onConfirmClicked { onBlockUser(userIdBlock) }
                            .build().show(context)
                    }
                }
            }
        }
    }

    private fun onDeleteSubmission(postId: Int) {

        if (!CheckNetwork.isNetworkAvailable(this)) {
            DialogInfoUtility.getInstance().showMessage(
                getString(R.string.app_name),
                resources.getString(
                    R.string.no_internet_connection
                ), this
            )
        } else {
            showDialog(this, getString(R.string.connecting_msg))
            challengeEntriesViewModel.deleteEntries(postId)
        }
    }

    private fun handleEdit(item: EntriesModel) {
        val intent = Intent(this, ActivityPostMedia::class.java)
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        intent.putExtra(ConstantBundleKey.BUNDLE_ENTRIES_MODEL, item)
        ActivityCompat.startActivityForResult(this, intent, Constants.REQUEST_EDIT_SUBMISSION, options.toBundle())
    }

    private fun handleReportClick(isReported: Boolean, challengeId: Int, position: Int) {
        if (isReported) {
            unReportPost(challengeId)
        } else {
            showDialogReportItem(challengeId)
        }
    }

    private fun showDialogReportItem(challengeId: Int) {
        val dialogReport = DialogReport.newInstance()
        dialogReport.setChooseReportListenner { reason ->
            Timber.e("report reason %s", reason)
            reportPost(challengeId, reason)
        }
        dialogReport.show((this).supportFragmentManager, "Report")
    }

    private fun reportPost(challengeId: Int, reason: String) {
        challengeEntriesViewModel.reportEntries(challengeId, reason, Constants.REPORT_TYPE)
    }

    private fun unReportPost(challengeId: Int) {
        challengeEntriesViewModel.reportEntries(challengeId, "", Constants.UNREPORT_TYPE)
    }

    internal fun handleFollowUnFollow(isFollow: Boolean, userID: Int, displayName: String) {
        if (isFollow) {
            DialogUtil.showConfirmUnFollowUser(this, displayName) { unFollowUser(userID) }
        } else {
            followUser(userID)
        }
    }

    private fun unFollowUser(userID: Int) {
        challengeEntriesViewModel.unFollowUser(userID)
    }

    private fun followUser(userID: Int) {
        challengeEntriesViewModel.followUser(userID)
    }

    private fun onBlockUser(userId: Int) {
        challengeEntriesViewModel.blockUser(userId)
    }

    private fun bindContentPost(title: String?) {
        if (title != null) {
            AutoLinkUtil.addAutoLinkMode(tvDescription)
            tvDescription.visibility = View.VISIBLE
            tvDescription.setAutoLinkText(title.decodeEmoji())
            tvDescription.setAutoLinkOnClickListener(AutoLinkUtil.newListener(this as BaseActivity))
        } else {
            tvDescription.visibility = View.GONE
        }
    }

    private fun showComments(model: EntriesModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intent = ActivityComments.newIntent(this, model.id!!, model.userId!!, CommentsType.ENTRIES, 0)
        ActivityCompat.startActivityForResult(
            this,
            intent,
            Constants.COMMENT_REQUEST,
            options.toBundle()
        )
    }

    private fun setViewMoreView(totalCommentCount: Int) {
        if (totalCommentCount > Constants.NUMBER_COMMENT_SHOW) {
            tvMoreComments.text = getString(R.string.view_all_for, totalCommentCount.toString())
            tvMoreComments.visibility = View.VISIBLE
        } else {
            tvMoreComments.visibility = View.GONE
        }
    }

    private fun bindLikeCounts(count: Int) {
        if (count <= 0) {
            llLikesCount.visibility = View.GONE
        } else {
            var likeCountString: String = if (count > 1) {
                String.format(
                    getString(R.string.likes_count),
                    Utils.shortenNumber(count.toLong())
                )
            } else {
                String.format(getString(R.string.like_count), count)
            }
            llLikesCount.visibility = View.VISIBLE
            tvLikesCount.text = likeCountString
        }
    }

    private fun bindCommentList(commentList: List<ItemClassComments>?) {
        if (commentList == null) return
        val commentListSize = commentList.size
        if (commentListSize <= 0) {
            commentListLayout.visibility = View.GONE
            return
        }

        if (commentListLayout.childCount > 0) commentListLayout.removeAllViews()
        commentListLayout.visibility = View.VISIBLE

        var firstPositionIsShown = 0
        if (commentListSize > Constants.NUMBER_COMMENT_SHOW) {
            firstPositionIsShown = commentListSize - Constants.NUMBER_COMMENT_SHOW
        }
        var i = firstPositionIsShown
        while (i in 0..(commentListSize - 1)) {
            val comment = commentList[i]
            val tvCommentUserName =
                LayoutInflater.from(applicationContext).inflate(
                    R.layout.comment_newfeed_row,
                    null
                ) as ExpandableTextView
            AutoLinkUtil.addAutoLinkMode(tvCommentUserName)
            tvCommentUserName.setAutoLinkOnClickListener(AutoLinkUtil.newListener(this as BaseActivity))
            tvCommentUserName.setMentionModeColor(Color.parseColor("#BBBBBB"))
            if (!comment.displayName.isNullOrEmpty()) {
                val displayname = StringUtil.decodeString(comment.userName?.toUserName())
                val contentShow =
                    StringUtil.decodeString(displayname) + " " + StringUtil.decodeString(comment.message)
                val start = contentShow.indexOf(displayname)
                val end = contentShow.indexOf(displayname) + displayname.length
                val clickUSerName = object : TouchableSpan() {
                    override fun onClick(widget: View) {
                        if (comment.userId == null) return
                        if (comment.userId == AppsterApplication.mAppPreferences.userModel.userId) return
                        val options =
                            ActivityOptionsCompat.makeCustomAnimation(
                                applicationContext,
                                R.anim.push_in_to_right,
                                R.anim.push_in_to_left
                            )
                        ActivityCompat.startActivityForResult(
                            mActivity,
                            UserProfileActivity.newIntent(applicationContext, comment.userId!!, ""),
                            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
                        )

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.parseColor("#BBBBBB")
                    }
                }

                val mTypeFaceOpensansBold =
                    Typeface.create(
                        Typeface.createFromAsset(this.applicationContext.assets, "fonts/opensansbold.ttf"),
                        Typeface.NORMAL
                    )
                val commentsContentSpan = SpannableString(contentShow)
                commentsContentSpan.setSpan(RelativeSizeSpan(1.0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                commentsContentSpan.setSpan(
                    CustomTypefaceSpan("sans-serif", mTypeFaceOpensansBold),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                commentsContentSpan.setSpan(clickUSerName, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                //            commentsContentSpan.setSpan(clickComment, end, contentShow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCommentUserName.setAutoLinkText(commentsContentSpan)
//            tvCommentUserName.setOnClickListener({ v -> imgComment.performClick() })
                commentListLayout.addView(tvCommentUserName)
            }
            i++
        }
    }

    private fun setLikeImage() {
        if (model?.isLike!!) ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
        else ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.COMMENT_REQUEST -> if (data != null) getComment(data)
                Constants.REQUEST_EDIT_SUBMISSION -> if (data != null) getEntriesData(data)
            }
        }
    }

    private fun getEntriesData(data: Intent) {
        data.extras ?: return
        val updateItem: EntriesModel? = data.extras!!.getParcelable(ConstantBundleKey.BUNDLE_ENTRIES_MODEL)

        if (model != null && updateItem != null && model!!.id!! == updateItem.id) {
            model = updateItem
            setData(updateItem)
        }
    }

    private fun getComment(data: Intent) {
        val extras = data.extras ?: return
        val arrCommentReturn: ArrayList<ItemClassComments>? =
            extras.getParcelableArrayList<ItemClassComments>(ConstantBundleKey.BUNDLE_LIST_COMMENT) ?: return
        bindCommentList(arrCommentReturn)
        val commentCounts = extras.getInt(ConstantBundleKey.BUNDLE_COMMENT_COUNT)
        if (commentCounts >= 0) {
            model!!.commentCount = commentCounts
            setViewMoreView(commentCounts)
        }
    }

    private fun initVideoView(urlVideo: String) {
        if (urlVideo.isNullOrEmpty()) return
        if (isFinishing || isDestroyed) return
        rlTextureVideoView.visibility = View.VISIBLE
        imageEntries.visibility = View.GONE
        textureVideoView.visibility = View.VISIBLE

        stopVideo()
        if (mSurface != null) {
            mSurface!!.release()
            mSurface = null
        }

        mKsyMediaPlayer = KSYMediaPlayer.Builder(applicationContext).build()
        textureVideoView.surfaceTextureListener = mSurfaceTextureListener
        powerOnSurfaceTextureAvailable()

        mKsyMediaPlayer!!.setTimeout(10, 30)
        mKsyMediaPlayer!!.bufferTimeMax = 2f
        mKsyMediaPlayer!!.setBufferSize(15)

        mKsyMediaPlayer!!.setOnPreparedListener(mOnPreparedListener)
        mKsyMediaPlayer!!.setOnInfoListener(mOnInfoListener)
        mKsyMediaPlayer!!.setOnErrorListener(mOnErrorListener)
        mKsyMediaPlayer!!.setOnCompletionListener(mOnCompletionListener)
        volumeControlStream = AudioManager.STREAM_MUSIC

        try {
            mKsyMediaPlayer!!.dataSource = urlVideo
        } catch (e: IOException) {
            Timber.e(e.message)
        }

        mKsyMediaPlayer!!.prepareAsync()
        //        mKsyMediaPlayer.runInForeground();
        //        mKsyMediaPlayer.start();

    }

    private fun releaseSurface() {
        if (mSurface != null) {
            mSurface!!.release()
            mSurface = null
        }
    }

    private fun powerOnSurfaceTextureAvailable() {
        Timber.e("textureVideoView.isAvailable " + textureVideoView.isAvailable)
        if (textureVideoView.isAvailable) {
            Timber.e("powerOnSurfaceTextureAvailable")
            mSurfaceTextureListener.onSurfaceTextureAvailable(
                textureVideoView.surfaceTexture,
                textureVideoView.width,
                textureVideoView.height
            )
        }
    }

    private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            if (mSurface == null) {
                mSurface = Surface(surfaceTexture)
                Timber.e("Create setSurface")
                if (mKsyMediaPlayer != null) {
                    Timber.e("setSurface")
                    mKsyMediaPlayer!!.setSurface(mSurface)
                }

            }
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {}

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            if (mSurface != null) {
                mSurface!!.release()
                mSurface = null
            }
            return true
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
    }

    private val mOnPreparedListener = IMediaPlayer.OnPreparedListener {
        Timber.e("VideoPlayer============OnPrepared")

        mVideoWidth = mKsyMediaPlayer!!.videoWidth
        mVideoHeight = mKsyMediaPlayer!!.videoHeight
        Timber.e("Video w - %d, h - %d", mVideoWidth, mVideoHeight)
        val frameParrent = textureVideoView.layoutParams as FrameLayout.LayoutParams
        val videoViewSize = Math.max(rlTextureVideoView.width, rlTextureVideoView.height)
        if (mVideoWidth > mVideoHeight) {
            frameParrent.width = videoViewSize
            frameParrent.height = (mVideoHeight * (videoViewSize / mVideoWidth.toFloat())).toInt()
            Timber.e("mVideoWidth")
        } else {
            frameParrent.height = videoViewSize
            frameParrent.width = (mVideoWidth * (videoViewSize / mVideoHeight.toFloat())).toInt()
            Timber.e("mVideoHeight")
        }
        textureVideoView.layoutParams = frameParrent

        //             Set Video Scaling Mode
        mKsyMediaPlayer!!.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)

        //start player
        mKsyMediaPlayer!!.start()
    }

    private val mOnInfoListener = IMediaPlayer.OnInfoListener { _, i, _ ->
        when (i) {
            KSYMediaPlayer.MEDIA_INFO_BUFFERING_START -> Timber.e("======================Buffering Start.")
            KSYMediaPlayer.MEDIA_INFO_BUFFERING_END -> Timber.e("======================Buffering End.")
            KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START -> Timber.e("======================Audio Rendering Start.")
            KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                Timber.e("======================Video Rendering Start.")
                autoAnimationPlayVideo()
                if (mSurface != null) mKsyMediaPlayer!!.setSurface(mSurface)
            }
            KSYMediaPlayer.MEDIA_INFO_RELOADED -> {
                Timber.e("======================Succeed to mPlayerReload video.")
            }
        }
        false
    }

    private val mOnErrorListener = IMediaPlayer.OnErrorListener { _, what, extra ->
        Timber.e("OnErrorListener, Error:$what,extra:$extra")
        when (what) {
            KSYMediaPlayer.MEDIA_ERROR_TIMED_OUT -> Timber.e("MEDIA_ERROR_TIMED_OUT$what,extra:$extra")
        }

        false
    }

    private val mOnCompletionListener = IMediaPlayer.OnCompletionListener {
        Timber.e("OnCompletionListener, play complete................")
        mKsyMediaPlayer!!.seekTo(0)
        mKsyMediaPlayer!!.start()
    }

    private fun muteUnMuteVideo() {
        muteUnMuteVideo(null)
    }

    private fun muteUnMuteVideo(isMute: Boolean?) {
        var isMute = isMute
        if (isMute == null) {
            isMute = AppsterApplication.mAppPreferences.isMuteVideos
        }

        if (mKsyMediaPlayer != null) {
            mKsyMediaPlayer!!.setPlayerMute(if (isMute) 1 else 0)
        }
//        onOffVolume.setBackgroundResource(if (isMute) R.drawable.volume_off else R.drawable.volume_on)
        AppsterApplication.mAppPreferences.isMuteVideos = !isMute
    }

    private fun autoAnimationPlayVideo() {
        val fadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.video_auto_fade_out)
        imageEntries.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                imageEntries.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun stopVideo() {
        if (mKsyMediaPlayer != null) {
            mKsyMediaPlayer!!.stop()
            mKsyMediaPlayer!!.release()
            mKsyMediaPlayer = null
        }
    }
}