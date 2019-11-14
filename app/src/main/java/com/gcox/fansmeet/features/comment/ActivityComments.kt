package com.gcox.fansmeet.features.comment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.comments_activity.*
import kotlinx.android.synthetic.main.view_user_input_text.*
import java.util.*
import com.gcox.fansmeet.util.view.CustomScrollListener
import timber.log.Timber
import android.support.v7.widget.LinearLayoutManager
import com.gcox.fansmeet.features.profile.userprofile.RecyclerViewPositionHelper
import com.gcox.fansmeet.features.profile.userprofile.UserProfileActivity
import com.gcox.fansmeet.util.StringUtil


/**
 * Created by User on 9/28/2015.
 */
class ActivityComments : BaseToolBarActivity(), AdapterListComments.CommentCallback {

    private var profileId: Int? = null
    private val commentsViewModel: CommentsViewModel by viewModel()
    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false
    private var postId: Int? = 0
    private val listComments = ArrayList<ItemClassComments>()
    private var adapter: AdapterListComments? = null
    private var commentsString: String? = ""
    private var isLoading = false
    private var positionHelper: RecyclerViewPositionHelper? = null
    private val mCurrentViewRect = Rect()
    private var commentType = CommentsType.CHALLENGES
    private var positionOnListview = 0
    private var commentCounts = -1

    companion object {
        @JvmStatic
        fun newIntent(context: Context, postId: Int, userId: Int, type: Int, positionOnListview: Int): Intent {
            val intent = Intent(context, ActivityComments::class.java)
            intent.putExtra(ConstantBundleKey.BUNDLE_POST_DETAIL_POST_ID, postId)
            intent.putExtra(ConstantBundleKey.BUNDLE_POST_DETAIL_USER_ID, userId)
            intent.putExtra(ConstantBundleKey.BUNDLE_COMMENT_TYPE, type)
            intent.putExtra(ConstantBundleKey.BUNDLE_COMMENT_POSITION, positionOnListview)
            return intent
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent?.extras
        if (bundle != null) {
            postId = bundle.getInt(ConstantBundleKey.BUNDLE_POST_DETAIL_POST_ID)
            profileId = bundle.getInt(ConstantBundleKey.BUNDLE_POST_DETAIL_USER_ID)
            commentType = bundle.getInt(ConstantBundleKey.BUNDLE_COMMENT_TYPE)
            positionOnListview = bundle.getInt(ConstantBundleKey.BUNDLE_COMMENT_POSITION)
            adapter = AdapterListComments(this, listComments, profileId!!)
            adapter!!.setCommentCallback(this)
            recyclerView.adapter = adapter!!
        }
        observeData()
        utility = DialogInfoUtility()
        if (profileId != null) getData(true)

    }

    override fun getLayoutContentId(): Int {
        return R.layout.comments_activity
    }

    override fun init() {
        setTopBarTile(getString(R.string.comment))
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { v -> onBackPressed() }
        handleTurnoffMenuSliding()
        positionHelper = RecyclerViewPositionHelper.createHelper(recyclerView)
        ll_owner.setOnClickListener { Utils.hideSoftKeyboard(this) }
        btnSend.setOnClickListener { addComment() }
        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                scrollBottom()
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisibleItemPosition = positionHelper!!.findFirstVisibleItemPosition()
                    val itemViewHolder =
                        recyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition)
                    if (isRecyclerViewAtTop() && getVisibilityPercents(itemViewHolder!!.itemView) == 100) {
                        //your recycler view reached Top do some thing
                        if (!isEnded && !isLoading) {
                            commentsViewModel.getCommentsList(postId!!, nextId, 7, commentType)
                            isLoading = true
                            Timber.e("TOPPPPP")
                        }
                    }
                }
            }
        })
    }


    fun getVisibilityPercents(view: View): Int {
        var percents = 100
        view.getLocalVisibleRect(mCurrentViewRect)
        val height = view.height
        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = Math.abs((height - mCurrentViewRect.top) * 100 / height)
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height
        }
        return percents
    }

    private fun viewIsPartiallyHiddenBottom(height: Int): Boolean {
        return mCurrentViewRect.bottom in 1..(height - 1)
    }

    private fun viewIsPartiallyHiddenTop(): Boolean {
        return mCurrentViewRect.top > 0
    }

    private fun refreshData() {
        nextId = Constants.WEB_SERVICE_START_PAGE
        getData(true)
    }

    private fun observeData() {
        commentsViewModel.getError().observe(this, Observer {
            dismissDialog()
            handleError(it?.message, it?.code!!)
        })

        commentsViewModel.getCommentsListResponse.observe(this, Observer {
            dismissDialog()
            if (nextId == Constants.WEB_SERVICE_START_PAGE) {
                listComments.addAll(it?.result!!)
                adapter?.notifyDataSetChanged()
                scrollBottom()
            } else {
                listComments.addAll(0, it?.result!!)
                adapter?.notifyDataSetChanged()
            }

            isLoading = false
            isEnded = it.isEnd
            nextId = it.nextId
        })

        commentsViewModel.getCommentResponse.observe(this, Observer {
            val item = ItemClassComments(
                postId,
                it?.message,
                AppsterApplication.mAppPreferences.userModel.displayName,
                AppsterApplication.mAppPreferences.userModel.userId,
                AppsterApplication.mAppPreferences.userModel.userImage,
                it?.created,
                AppsterApplication.mAppPreferences.userModel.gender,
                AppsterApplication.mAppPreferences.userModel.userName
            )
            listComments.add(item)
            adapter?.notifyDataSetChanged()
            scrollBottom()
            inputComments.setText("")
            commentCounts = it?.commentCounts!!
        })

        commentsViewModel.getDeleteCommentResponse.observe(this, Observer {
            if (it != null) {
                if (listComments.size > it.position - 1) {
                    if (listComments[it.position].commentId == it.commentId) {
                        listComments.removeAt(it.position)
                        adapter?.notifyDataSetChanged()
                    }
                }
                if (it.commentCount != null) commentCounts = it.commentCount!!
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CODE_VIEW_USER_PROFILE -> {
                    if (data != null) {
                        val isBlock = data.getBooleanExtra(UserProfileActivity.ARG_USER_BLOCKED, false)
                        if (isBlock) {
                            refreshData()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun isRecyclerViewAtTop(): Boolean {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val pos = linearLayoutManager.findFirstVisibleItemPosition()
        val view = linearLayoutManager.findViewByPosition(pos)
        if (linearLayoutManager.findViewByPosition(pos) != null && view != null && view.top == 0 && pos == 0) {
            return true
        }
        return false
    }

    private fun scrollBottom() {
        recyclerView.postDelayed(
            Runnable { recyclerView.scrollToPosition(recyclerView.adapter!!.itemCount - 1) },
            100
        )
    }

    private fun scrollTop() {
        val layoutManager = recyclerView
            .layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }

    private fun addComment() {
        commentsString = inputComments.text.toString().trim()
        if (!CheckNetwork.isNetworkAvailable(this)
            || commentsString == null
            || commentsString == ""
        ) run { } else if (!CheckNetwork.isNetworkAvailable(this)) run {
            utility.showMessage(
                getString(R.string.app_name),
                resources.getString(
                    R.string.no_internet_connection
                ), this
            )
        } else {
            val tagUsers = inputComments.stringTaggedUsersIdAndClear
            val request = AddCommentRequestModel(postId, StringUtil.encodeString(commentsString), tagUsers)
            commentsViewModel.addComment(request, commentType)
        }
    }

    private fun getData(isShowDialog: Boolean) {
        if (isShowDialog) showDialog(this, getString(R.string.connecting_msg))
        commentsViewModel.getCommentsList(postId!!, nextId, Constants.PAGE_LIMITED, commentType)
    }

    override fun onBackPressed() {
        setDataResult()
    }

    private fun setDataResult() {
        val arrResult = ArrayList<ItemClassComments>()

        var count = listComments.size
        if (count > Constants.NUMBER_COMMENT_SHOW) {
            count = Constants.NUMBER_COMMENT_SHOW
        }

        var i = listComments.size - count
        while (i < listComments.size && i >= 0) {
            arrResult.add(listComments[i])
            i++
        }
        if (intent != null) {
            intent!!.putExtra(ConstantBundleKey.BUNDLE_COMMENT_POSITION, positionOnListview)
            intent!!.putExtra(ConstantBundleKey.BUNDLE_COMMENT_COUNT, commentCounts)
            intent!!.putParcelableArrayListExtra(ConstantBundleKey.BUNDLE_LIST_COMMENT, arrResult)
            intent!!.putExtra(ConstantBundleKey.BUNDLE_POST_ID_KEY, postId)
        }

        setResult(RESULT_OK, intent)
        Utils.hideSoftKeyboard(this)
        finish()
    }

    override fun onDeleteComment(position: Int) {
        if (listComments.size >= position - 1 && position >= 0) {
            val item = listComments[position]
            if (!CheckNetwork.isNetworkAvailable(this)) {
                utility.showMessage(
                    getString(R.string.app_name),
                    resources.getString(
                        R.string.no_internet_connection
                    ), this
                )
            } else {
                commentsViewModel.deleteComment(position, item.commentId!!, postId!!, commentType)
            }
        }
    }

    override fun onRowClick(position: Int) {
    }

    override fun onUserImageClicked(position: Int, item: ItemClassComments) {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        ActivityCompat.startActivityForResult(
            this,
            UserProfileActivity.newIntent(applicationContext, item.userId!!, item.userName!!),
            Constants.REQUEST_CODE_VIEW_USER_PROFILE, options.toBundle()
        )
    }
}
