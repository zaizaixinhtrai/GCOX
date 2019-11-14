package com.gcox.fansmeet.features.challengedetail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.appster.extensions.decodeEmoji
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.features.comment.ActivityComments
import com.gcox.fansmeet.features.comment.CommentsType
import com.gcox.fansmeet.features.challengeentries.ChallengeEntriesActivity
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailEntriesViewHolder
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailViewHolder
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesModel
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.post.BundleMedia
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.models.eventbus.EventBusRefreshEntries
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.FileUtility
import com.gcox.fansmeet.util.UiUtils
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_join_challenge.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.util.*

class ChallengeDetailActivity : BaseToolBarActivity(), ChallengeDetailViewHolder.OnClickListener,
    ChallengeDetailEntriesViewHolder.OnClickListener, OnLoadMoreListenerRecyclerView {

    private var adapter: ChallengeDetailAdapter? = null
    private val listCelebrity = arrayListOf<DisplayableItem>()
    private var challengeId: Int? = 0
    private val joinChallengeViewModel: ChallengeDetailViewModel by viewModel()
    private var itemJoin: CelebrityModel? = null
    private var needViewEntries: Boolean? = false
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false

    companion object {
        const val BUNDLE_NEED_VIEW_ENTRIES = "need_view_entries"
        @JvmStatic
        fun newIntent(context: Context, challengeId: Int, needScrollToEntries: Boolean): Intent {
            val intent = Intent(context, ChallengeDetailActivity::class.java)
            intent.putExtra(Constants.CHALLENGE_ID, challengeId)
            intent.putExtra(BUNDLE_NEED_VIEW_ENTRIES, needScrollToEntries)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        handleTurnoffMenuSliding()
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        observeData()
        challengeId = intent?.extras?.getInt(Constants.CHALLENGE_ID)
        needViewEntries = intent?.extras?.getBoolean(BUNDLE_NEED_VIEW_ENTRIES)

        if (challengeId != null) {
            showDialog(this, getString(R.string.connecting_msg))
            joinChallengeViewModel.getChallenge(challengeId!!)
        }
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_join_challenge
    }

    override fun init() {
        initRecyclerView()
        UiUtils.setColorSwipeRefreshLayout(swiperefresh!!)
        swiperefresh!!.setOnRefreshListener { this.refreshData() }
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

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded && challengeId != null) joinChallengeViewModel.getChallengeEntries(challengeId!!, nextId)
        }
    }

    private fun refreshData() {
        if (!CheckNetwork.isNetworkAvailable(this)) {
            return
        }

        if (challengeId != null) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            swiperefresh?.isRefreshing = false
            showDialog(this, getString(R.string.connecting_msg))
            listCelebrity.clear()
            adapter?.notifyDataSetChanged()
            joinChallengeViewModel.getChallenge(challengeId!!)
        }
    }

    private fun getComment(data: Intent) {
        val extras = data.extras ?: return
        val arrCommentReturn: ArrayList<ItemClassComments>? =
            extras.getParcelableArrayList<ItemClassComments>(ConstantBundleKey.BUNDLE_LIST_COMMENT) ?: return
        if (listCelebrity.isNotEmpty()) {
            if (listCelebrity[0] is CelebrityModel) {
                val item = Objects.requireNonNull(listCelebrity[0]) as CelebrityModel
                item.comments = arrCommentReturn
                listCelebrity[0] = item
                adapter?.notifyItemChanged(0)
            }
        }
    }

    override fun onCommentClicked(item: CelebrityModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intent = ActivityComments.newIntent(this, item.id!!, item.userId!!, CommentsType.CHALLENGES, 0)
        ActivityCompat.startActivityForResult(
            this,
            intent,
            Constants.COMMENT_REQUEST,
            options.toBundle()
        )
    }

    override fun onImageClicked(item: CelebrityModel) {
    }

    override fun onEntriesClicked(item: JoinChallengeEntriesModel) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
        val intentGift = ChallengeEntriesActivity.newIntent(this, item.id!!)
        ActivityCompat.startActivityForResult(
            this,
            intentGift,
            Constants.REQUEST_CHALLENGE_ENTRIES_ACTIVITY,
            options.toBundle()
        )
    }

    override fun onUserNameClicked(userId: Int?) {
        if (userId == null) return
        if (preventMultiClicks()) return
        if (userId == AppsterApplication.mAppPreferences.userModel.userId) return
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        ActivityCompat.startActivityForResult(
            this,
            UserProfileActivity.newIntent(applicationContext, userId, ""),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }

    override fun onJoinChallengeClicked(item: CelebrityModel) {

//        val options =
//            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
//        val intentGift = ActivityPostMedia.createIntent(this, item.id!!, 1)
//        ActivityCompat.startActivityForResult(
//            this,
//            intentGift,
//            Constants.REQUEST_CHALLENGE_DETAIL_ACTIVITY,
//            options.toBundle()
//        )
    }

    override fun onLikeClicked(item: CelebrityModel) {
        if (!item.isLike!!) joinChallengeViewModel.like(item.id!!)
        else joinChallengeViewModel.unlike(item.id!!)
    }

    private fun observeData() {
        joinChallengeViewModel.getError().observe(this, Observer {
            it?.let {
                handleError(it.message,it.code)
            }
            dismissDialog()
            swiperefresh?.isRefreshing = false
        })

        joinChallengeViewModel.getChallenge.observe(this, Observer {
            if (it != null) {
                itemJoin = it
                rightButton?.visibility = View.GONE
                joinChallengeViewModel.checkCanSubmitChallenge(it.id!!)
                listCelebrity.add(it)
                setTopBarTileNoCap(it.title?.decodeEmoji())
                adapter?.notifyDataSetChanged()
                nextId = Constants.WEB_SERVICE_START_PAGE
                isEnded = false
                joinChallengeViewModel.getChallengeEntries(challengeId!!, nextId)
                if (it.isReachSubmissionLimit != null) {
                    if (itemJoin!!.userId != AppsterApplication.mAppPreferences.userModel.userId) {
                        if (it.isReachSubmissionLimit!!) {
                            rightButton.text = getString(R.string.challengers_joined)
                            rightButton.background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.tool_bar_join_button_background)
                            rightButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.back_ground_btn_join))
                        } else {
                            rightButton.text = getString(R.string.challengers_join)
                            rightButton.background =
                                ContextCompat.getDrawable(applicationContext, R.drawable.join_challenge_background)
                            rightButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                        }
                        rightButton.setOnClickListener { onJoinClicked(itemJoin!!) }
                    }
                }
            }
        })

        joinChallengeViewModel.getChallengeEntries.observe(this, Observer {
            listCelebrity.addAll(it?.result!!)
            adapter?.notifyDataSetChanged()
            dismissDialog()
            swiperefresh?.isRefreshing = false
            if (needViewEntries != null && needViewEntries!!) scrollToEntries()
            nextId = it.nextId!!
            isEnded = it.isEnd!!
        })

        joinChallengeViewModel.getLikeResponse.observe(this, Observer {
            if (listCelebrity.isNotEmpty()) {
                if (listCelebrity[0] is CelebrityModel) {
                    val item = Objects.requireNonNull(listCelebrity[0]) as CelebrityModel
                    item.likeCount = it
                    item.isLike = true
                    listCelebrity[0] = item
                    adapter?.notifyItemChanged(0)
                }
            }
        })

        joinChallengeViewModel.getUnlikeResponse.observe(this, Observer {
            if (listCelebrity.isNotEmpty()) {
                if (listCelebrity[0] is CelebrityModel) {
                    val item = Objects.requireNonNull(listCelebrity[0]) as CelebrityModel
                    item.likeCount = it
                    item.isLike = false
                    listCelebrity[0] = item
                    adapter?.notifyItemChanged(0)
                }
            }
        })

        joinChallengeViewModel.canSubmitChallenge.observe(this, Observer {
            dismissDialog()
            visibleRightButton()
        })
    }

    private fun scrollToEntries() {
        val view = recyclerView.layoutManager!!.findViewByPosition(0)
        recyclerView.postDelayed(
            Runnable { recyclerView.scrollBy(0, view!!.height) },
            100
        )
        needViewEntries = false
    }

    private fun initRecyclerView() {
        adapter = ChallengeDetailAdapter(null, listCelebrity, this, this)
        recyclerView.adapter = adapter
        val manager = GridLayoutManager(this, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return if (listCelebrity[position] is CelebrityModel) manager.spanCount else 1
            }
        }

        recyclerView.layoutManager = manager
        recyclerView.setOnLoadMoreListener(this)
    }

    private fun onJoinClicked(item: CelebrityModel) {
        itemJoin?.let {
            if(it.isReachSubmissionLimit!=null){
                if (item.userId != AppsterApplication.mAppPreferences.userModel.userId) {
                    joinChallengeClicked()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.can_not_join_challenge),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startPostWithMedia(fileUri: Uri, type: Int) {
        val intent = Intent(this, ActivityPostMedia::class.java)
        val bundleMedia = BundleMedia()
        bundleMedia.setIsPost(true)
        bundleMedia.key = ConstantBundleKey.BUNDLE_MEDIA_KEY
        bundleMedia.type = type
        bundleMedia.uriPath = fileUri.toString()
        bundleMedia.setIntent(intent)
        if (itemJoin != null) {
            bundleMedia.challengeId = itemJoin!!.id!!
            bundleMedia.isSubmissionChallenge = true
        }

        val options =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
        ActivityCompat.startActivityForResult(this, intent, Constants.POST_REQUEST, options.toBundle())
    }

    private fun joinChallengeClicked() {
        when (itemJoin!!.mediaType) {
            Constants.POST_TYPE_IMAGE, Constants.POST_TYPE_CHALLENGE_SELFIE -> showPicPopUp()
            Constants.POST_TYPE_VIDEO -> showVideosPopUp()
            Constants.POST_TYPE_QUOTES -> startPostWithMedia(Uri.parse(""), Constants.POST_TYPE_QUOTES)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.COMMENT_REQUEST -> if (data != null) getComment(data)

                Constants.REQUEST_PIC_FROM_LIBRARY -> {
                    val imageCroppedURI: Uri
                    try {
                        imageCroppedURI = getOutputMediaFileUri(FileUtility.MEDIA_TYPE_IMAGE_CROPPED)
                    } catch (e: NullPointerException) {
                        Timber.d(e)
                        return
                    }

                    if (data?.data != null) {
                        fileUri = data.data
                        performCrop(fileUri, imageCroppedURI)
                    }
                }

                Constants.REQUEST_PIC_FROM_CROP -> {
                    if (data != null) {
                        val resultUri = UCrop.getOutput(data)
                        if (resultUri != null) {
                            if (itemJoin != null) {
                                if (itemJoin!!.mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                                    openImageEditorScreen(this, fileUri, itemJoin!!.selfieImage!!)
                                } else {
                                    startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                                }
                            }
                        } else {
                            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                Constants.REQUEST_PIC_FROM_CAMERA -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        if (itemJoin != null) {
                            if (itemJoin!!.mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                                openImageEditorScreen(this, fileUri, itemJoin!!.selfieImage!!)
                            } else {
                                startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                            }
                        }
                    }
                }

                Constants.CAMERA_VIDEO_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        Timber.e("mRecordUrl $fileUri")
                        startPostWithMedia(fileUri, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.PICK_VIDEO_FROM_LIBRARY_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        loadVideoAfterPickFromGallery(fileUri)
                    }
                }

                Constants.VIDEO_TRIMMED_REQUEST -> {
                    if (data != null) {
                        val urlImage = data.getStringExtra(Constants.VIDEO_PATH)
                        val image = Uri.fromFile(File(urlImage))
                        startPostWithMedia(image, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.POST_REQUEST -> {
                    listCelebrity.clear()
                    adapter!!.notifyDataSetChanged()
                    joinChallengeViewModel.getChallenge(challengeId!!)
                }

                Constants.REQUEST_PHOTO_EDITOR_ACTIVITY -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                    }
                }
            }
        }
    }

    override fun onShareClicked(item: CelebrityModel, position: Int) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                SocialManager.getInstance().getShareContent(
                    applicationContext,
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(event: EventBusRefreshEntries) {
        if (event.isRefresh)
            refreshData()
    }
}