package com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders

import android.app.Activity
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.Typeface
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.RelativeSizeSpan
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.PopupMenu
import com.appster.extensions.decodeEmoji
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.appster.extensions.toUserName
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.customview.CustomTypefaceSpan
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkUtil
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkUtil.addAutoLinkMode
import com.gcox.fansmeet.customview.autolinktextview.TouchableSpan
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityComment
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.manager.VideosManager
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.util.*
import com.gcox.fansmeet.util.view.ExpandableTextView
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.ksyun.media.player.IMediaPlayer
import com.ksyun.media.player.KSYMediaPlayer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_post_detail.view.*
import kotlinx.android.synthetic.main.feed_header_view.view.*
import kotlinx.android.synthetic.main.view_action_like_comment.view.*
import timber.log.Timber


class UserPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mVideoPlayerManager: VideosManager? = null
    private var mKsyMediaPlayer: KSYMediaPlayer? = null
    private var mSurface: Surface? = null
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var model: CelebrityModel? = null
    private var mTypeFaceOpensansBold: Typeface? = null
    private var clickListener: OnClickListener? = null
    private var compositeDisposable: CompositeDisposable? = null

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): UserPostViewHolder {
            return UserPostViewHolder(parent.inflate(layout))
        }
    }

    init {
        mTypeFaceOpensansBold =
            Typeface.create(
                Typeface.createFromAsset(itemView.context.assets, "fonts/opensansbold.ttf"),
                Typeface.NORMAL
            )
    }

    fun bindTo(item: CelebrityModel, listener: OnClickListener?, videoPlayerManager: VideosManager) {
        with(itemView) {
            model = item
            clickListener = listener
            mVideoPlayerManager = videoPlayerManager
            btJoinChallenge.visibility = View.GONE
            itemView.tvUserName.text = item.displayName?.decodeEmoji()
            itemView.useImage.loadImg(item.userImage)
            mediaImage.loadImg(item.image)
            tvTimer.text = SetDateTime.partTimeForFeedItem(item.created, context)
            updateLikeCount(item.likeCount)

            btJoinChallenge.visibility = View.GONE
            tvViewDetails.visibility = View.GONE
            btJoin.visibility = View.GONE

            when (item.mediaType) {

                Constants.POST_TYPE_VIDEO -> {
                    media_image_fl.visibility = View.VISIBLE
                    onOffVolume.visibility = View.VISIBLE
                    btnVideo.visibility = View.VISIBLE
                    rlTextureVideoView.visibility = View.VISIBLE
                    bindTitle(item.description)
                    tvContentPost.visibility = View.GONE
                    initPlayer()
                }
                Constants.POST_TYPE_IMAGE, Constants.POST_TYPE_CHALLENGE_SELFIE -> {
                    media_image_fl.visibility = View.VISIBLE
                    btnVideo.visibility = View.GONE
                    onOffVolume.visibility = View.GONE
                    bindTitle(item.description)
                    tvContentPost.visibility = View.GONE
                }
                else -> {
                    rlTextureVideoView.visibility = View.GONE
                    onOffVolume.visibility = View.GONE
                    btnVideo.visibility = View.GONE
                    media_image_fl.visibility = View.GONE
                    bindContentPost(item.description)
                    tvTitle.visibility = View.GONE
                    tvContentPost.visibility = View.VISIBLE
                }
            }

            onOffVolume.setOnClickListener { v -> muteUnMuteVideo() }
            //comment section
            clearCommentList()
            setViewMoreView(item.commentCount!!)
            bindCommentList(item.comments)

            item.isLike?.let {
                if (item.isLike!!) ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
                else ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
            }
            ivLike.setOnClickListener {
                if (model!!.postType == Constants.CHALLENGE_SUBMISSION) {
                    if (item.isLike == false) likeEntries(model!!.id!!) else unLikeEntries(model!!.id!!)
                } else {
                    if (item.isLike == false) likePost(model!!.id!!) else unLikePost(model!!.id!!)
                }
            }

            imgComment.setOnClickListener { listener?.onCommentClicked(item, adapterPosition) }
            tvViewAllComment.setOnClickListener { imgComment.performClick() }
            commentListLayout.setOnClickListener { imgComment.performClick() }

            moreOption.setOnClickListener { listener?.onOptionMenuClicked(item, adapterPosition) }
            imgShare.setOnClickListener { listener?.onShareClicked(item, adapterPosition) }
        }
    }

    private fun updateLikeCount(likeCount: Int?) {
        if (likeCount == null) return
        if (likeCount <= 0) {
            itemView.tvLikesCount.visibility = View.GONE
        } else {
            var likeCountString: String = if (likeCount!! > 1) {
                String.format(
                    itemView.context.getString(R.string.likes_count),
                    Utils.shortenNumber(likeCount.toLong())
                )
            } else {
                String.format(itemView.context.getString(R.string.like_count), likeCount)
            }
            itemView.tvLikesCount.visibility = View.VISIBLE
            itemView.tvLikesCount.text = likeCountString
        }
    }

    private fun likeEntries(challengeId: Int) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().likeEntries(AppsterUtility.getAuth(), challengeId)
                .subscribe({ likePostResponseModel ->
                    if (likePostResponseModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        itemView.ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
                        model!!.likeCount = likePostResponseModel.data
                        model!!.isLike = true
                        updateLikeCount(model!!.likeCount)
                    }
                    dispose()
                }, { error ->
                    dispose()
                })
        )
    }

    private fun likePost(challengeId: Int) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().likePost(AppsterUtility.getAuth(), challengeId)
                .subscribe({ likePostResponseModel ->
                    if (likePostResponseModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        itemView.ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
                        model!!.likeCount = likePostResponseModel.data
                        model!!.isLike = true
                        updateLikeCount(model!!.likeCount)
                    }
                    dispose()
                }, { error ->
                    dispose()
                })
        )
    }

    private fun unLikePost(challengeId: Int) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            AppsterWebServices.get().unlike(AppsterUtility.getAuth(), challengeId)
                .subscribe({ likePostResponseModel ->
                    if (likePostResponseModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        itemView.ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
                        model!!.likeCount = likePostResponseModel.data
                        model!!.isLike = false
                        updateLikeCount(model!!.likeCount)
                    }
                    dispose()
                }, { error ->
                    dispose()
                })
        )
    }

    private fun unLikeEntries(challengeId: Int) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            AppsterWebServices.get().unlikeEntries(AppsterUtility.getAuth(), challengeId)
                .subscribe({ likePostResponseModel ->
                    if (likePostResponseModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        itemView.ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
                        model!!.likeCount = likePostResponseModel.data
                        model!!.isLike = false
                        updateLikeCount(model!!.likeCount)
                    }
                    dispose()
                }, { error ->
                    dispose()
                })
        )
    }

    private fun dispose() {
        if (compositeDisposable != null && !compositeDisposable!!.isDisposed) compositeDisposable!!.dispose()
    }

    private fun bindContentPost(title: String?) {
        if (title != null) {
            addAutoLinkMode(itemView.tvContentPost)
            itemView.tvContentPost.visibility = View.VISIBLE
            itemView.tvContentPost.setAutoLinkText(title.decodeEmoji())
            itemView.tvContentPost.setAutoLinkOnClickListener(AutoLinkUtil.newListener(itemView.context as BaseActivity))
        }
    }

    private fun bindTitle(title: String?) {
        if (title.isNullOrEmpty()) itemView.tvTitle.visibility = View.GONE
        else {
            addAutoLinkMode(itemView.tvTitle)
            itemView.tvTitle.visibility = View.VISIBLE
            itemView.tvTitle.setAutoLinkText(title.decodeEmoji())
            itemView.tvTitle.setAutoLinkOnClickListener(AutoLinkUtil.newListener(itemView.context as BaseActivity))
        }
    }

    private fun bindCommentList(commentList: List<ItemClassComments>?) {
        if (commentList == null) return
        val commentListSize = commentList.size
        if (commentListSize <= 0) {
            itemView.commentListLayout.visibility = View.GONE
            return
        }

        itemView.commentListLayout.visibility = View.VISIBLE

        var firstPositionIsShown = 0
        if (commentListSize > Constants.NUMBER_COMMENT_SHOW) {
            firstPositionIsShown = commentListSize - Constants.NUMBER_COMMENT_SHOW
        }
        var i = firstPositionIsShown
        while (i in 0..(commentListSize - 1)) {
            val comment = commentList[i]
            val tvCommentUserName =
                LayoutInflater.from(itemView.context).inflate(R.layout.comment_newfeed_row, null) as ExpandableTextView
            AutoLinkUtil.addAutoLinkMode(tvCommentUserName)
            tvCommentUserName.setAutoLinkOnClickListener(AutoLinkUtil.newListener(itemView.context as BaseActivity))
            tvCommentUserName.setMentionModeColor(Color.parseColor("#BBBBBB"))
            if (!comment.displayName.isNullOrEmpty()) {
                val displayname = StringUtil.decodeString(comment.userName?.toUserName())
                val contentShow =
                    StringUtil.decodeString(displayname) + " " + StringUtil.decodeString(comment.message)
                val start = contentShow.indexOf(displayname)
                val end = contentShow.indexOf(displayname) + displayname.length
                val clickUSerName = object : TouchableSpan() {
                    override fun onClick(widget: View) {
                        clickListener?.onUserNameClicked(comment.userId)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.parseColor("#BBBBBB")
                    }
                }

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
                itemView.commentListLayout.addView(tvCommentUserName)
            }
            i++
        }
    }

    private fun setViewMoreView(totalCommentCount: Int) {
        if (totalCommentCount > Constants.NUMBER_COMMENT_SHOW) {
            itemView.tvViewAllComment.text =
                itemView.context.getString(R.string.view_all_for, totalCommentCount.toString())
            itemView.tvViewAllComment.visibility = View.VISIBLE
            itemView.tvViewAllComment.setOnClickListener { v ->

            }
        } else {
            itemView.tvViewAllComment.visibility = View.GONE
        }
    }

    private fun clearCommentList() {
        if (itemView.commentListLayout.childCount > 0) {
            itemView.commentListLayout.removeAllViews()
        }
    }

    private fun initPlayer() {
        if (mKsyMediaPlayer != null) {
            mKsyMediaPlayer!!.stop()
            mKsyMediaPlayer!!.release()
            mKsyMediaPlayer = null
        }
        mVideoPlayerManager?.resetMediaPlayer()
        if (itemView.textureVideoView != null) {
            releaseSurface()
            mKsyMediaPlayer = KSYMediaPlayer.Builder(itemView.context.applicationContext).build()
            itemView.textureVideoView.surfaceTextureListener = mSurfaceTextureListener
            powerOnSurfaceTextureAvailable()

            mKsyMediaPlayer!!.setTimeout(10, 30)
            mKsyMediaPlayer!!.bufferTimeMax = 2f
            mKsyMediaPlayer!!.setBufferSize(15)

            mKsyMediaPlayer!!.setOnPreparedListener(mOnPreparedListener)
            mKsyMediaPlayer!!.setOnInfoListener(mOnInfoListener)
            mKsyMediaPlayer!!.setOnErrorListener(mOnErrorListener)
            mKsyMediaPlayer!!.setOnCompletionListener(mOnCompletionListener)
            muteUnMuteVideo(true)
        }
    }

    private fun initVideoView(urlVideo: String) {
        Timber.e("isAttachedToWindow " + itemView.textureVideoView.isAttachedToWindow)
        if (itemView.textureVideoView.isAttachedToWindow) {
            initPlayer()
            mVideoPlayerManager!!.playVideos(mKsyMediaPlayer, urlVideo) {
                itemView.mediaImage?.visibility = View.VISIBLE
            }
        }
    }

    private fun releaseSurface() {
        if (mSurface != null) {
            mSurface!!.release()
            mSurface = null
        }
    }

    private fun powerOnSurfaceTextureAvailable() {
        Timber.e("textureVideoView.isAvailable " + itemView.textureVideoView.isAvailable)
        if (itemView.textureVideoView.isAvailable) {
            Timber.e("powerOnSurfaceTextureAvailable")
            mSurfaceTextureListener.onSurfaceTextureAvailable(
                itemView.textureVideoView.surfaceTexture,
                itemView.textureVideoView.width,
                itemView.textureVideoView.height
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
        val frameParrent = itemView.textureVideoView.layoutParams as FrameLayout.LayoutParams
        val videoViewSize = Math.max(itemView.rlTextureVideoView.width, itemView.rlTextureVideoView.height)
        if (mVideoWidth > mVideoHeight) {
            frameParrent.width = videoViewSize
            frameParrent.height = (mVideoHeight * (videoViewSize / mVideoWidth.toFloat())).toInt()
            Timber.e("mVideoWidth")
        } else {
            frameParrent.height = videoViewSize
            frameParrent.width = (mVideoWidth * (videoViewSize / mVideoHeight.toFloat())).toInt()
            Timber.e("mVideoHeight")
        }
        itemView.textureVideoView.layoutParams = frameParrent

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
        itemView.onOffVolume.setBackgroundResource(if (isMute) R.drawable.volume_off else R.drawable.volume_on)
        AppsterApplication.mAppPreferences.isMuteVideos = !isMute
    }

    private fun autoAnimationPlayVideo() {
        val fadeOut = AnimationUtils.loadAnimation(itemView.context.applicationContext, R.anim.video_auto_fade_out)
        itemView.mediaImage.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                itemView.mediaImage.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun autoPlayVideo(url: String) {
        itemView.textureVideoView.visibility = View.VISIBLE
        initVideoView(url)
    }

    fun getPostData(): CelebrityModel {
        return model!!
    }

    private fun showOption(view: View, position: Int, listener: OnClickListener?) {
        val popupMenuOption = PopupMenu(itemView.context, view)
        popupMenuOption.menu.add(0, 0, 0, itemView.context.getString(R.string.newsfeed_menu_del_post))
//        if (showDeleteOption) {
//            popupMenuOption.menu.add(0, 0, 1, itemView.context.getString(R.string.stream_comment_delete_text))
//        }
        popupMenuOption.setOnMenuItemClickListener { item ->
            when (item.order) {
                0 -> DialogUtil.showConfirmDialog(
                    itemView.context as Activity,
                    itemView.context.getString(R.string.newsfeed_menu_del_post),
                    itemView.context.getString(R.string.do_you_want_to_delete_this_post),
                    itemView.context.getString(R.string.delete_comment_delete_button)
                ) {
                    //                    listener?.onDeletePost(model!!, position)
                }

            }
            true
        }

        popupMenuOption.show()
    }

    interface OnClickListener {
        fun onLikeClicked(item: CelebrityModel, position: Int)
        fun onCommentClicked(item: CelebrityModel, position: Int)
        fun onUserNameClicked(userId: Int?)
        fun onOptionMenuClicked(item: CelebrityModel, position: Int)
        fun onShareClicked(item: CelebrityModel, position: Int)
    }
}