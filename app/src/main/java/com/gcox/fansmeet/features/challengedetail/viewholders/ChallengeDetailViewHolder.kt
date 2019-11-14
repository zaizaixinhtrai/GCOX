package com.gcox.fansmeet.features.challengedetail.viewholders

import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.decodeEmoji
import com.appster.extensions.inflate
import com.appster.extensions.toUserName
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.customview.CustomTypefaceSpan
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkUtil
import com.gcox.fansmeet.customview.autolinktextview.TouchableSpan
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.util.StringUtil
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.util.view.ExpandableTextView
import kotlinx.android.synthetic.main.join_challenger_item.view.*
import kotlinx.android.synthetic.main.view_action_like_comment.view.*


class ChallengeDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mTypeFaceOpensansBold: Typeface? = null
    private var clickListener: OnClickListener?=null

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): ChallengeDetailViewHolder {
            return ChallengeDetailViewHolder(parent.inflate(layout))
        }
    }

    init {
        mTypeFaceOpensansBold =
            Typeface.create(
                Typeface.createFromAsset(itemView.context.assets, "fonts/opensansbold.ttf"),
                Typeface.NORMAL
            )
    }

    fun bindTo(item: CelebrityModel, listener: OnClickListener?) {
        with(itemView) {
            clickListener = listener
            btJoin.visibility = View.GONE
            tvViewDetails.visibility = View.GONE
            tvDescription.text = item.description?.decodeEmoji()
            tvPrizes.text = item.prizeText?.decodeEmoji()
            if (item.likeCount!! <= 0) {
                llLikesCount.visibility = View.GONE
            } else {
                var likeCountString: String
                if (item.likeCount!! > 1) {
                    likeCountString =
                        String.format(
                            context.getString(R.string.likes_count),
                            Utils.shortenNumber(item.likeCount!!.toLong())
                        )
                } else {
                    likeCountString = String.format(context.getString(R.string.like_count), item.likeCount)
                }

                tvLikesCount.text = likeCountString

            }

            btJoinChallenge.visibility = View.GONE
            btJoin.visibility = View.GONE
            tvEntriesCount.text = String.format(context.getString(R.string.challenges_entries_count), item.entryCount)
            if (item.isLike!!) ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
            else ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
            ivLike.setOnClickListener { listener?.onLikeClicked(item) }

            //comment section
            clearCommentList()
            setViewMoreView(item.commentCount!!)
            bindCommentList(item.comments)
            imgComment.setOnClickListener { listener?.onCommentClicked(item) }
            tvViewAllComment.setOnClickListener { listener?.onCommentClicked(item) }
            imgShare.setOnClickListener { listener?.onShareClicked(item, adapterPosition) }
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
        while (i in 0 until commentListSize) {
            val comment = commentList[i]
            val tvCommentUserName =
                LayoutInflater.from(itemView.context).inflate(R.layout.comment_newfeed_row, null) as ExpandableTextView
            tvCommentUserName.setMentionModeColor(Color.parseColor("#BBBBBB"))
            AutoLinkUtil.addAutoLinkMode(tvCommentUserName)
            tvCommentUserName.setAutoLinkOnClickListener(AutoLinkUtil.newListener(itemView.context as BaseActivity))
            if (!comment.displayName.isNullOrEmpty()) {
                val displayname = StringUtil.decodeString(comment.userName?.toUserName())
                val contentShow = displayname + " " + StringUtil.decodeString(comment.message)
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

    interface OnClickListener {
        fun onImageClicked(item: CelebrityModel)
        fun onJoinChallengeClicked(item: CelebrityModel)
        fun onLikeClicked(item: CelebrityModel)
        fun onCommentClicked(item: CelebrityModel)
        fun onUserNameClicked(userId:Int?)
        fun onShareClicked(item: CelebrityModel, position: Int)
    }
}