package com.gcox.fansmeet.features.profile.userprofile.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.decodeEmoji
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.util.SetDateTime
import kotlinx.android.synthetic.main.challenge_info_layout.view.*
import kotlinx.android.synthetic.main.feed_header_view.view.*
import kotlinx.android.synthetic.main.view_action_like_comment.view.*


class UserChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): UserChallengeViewHolder {
            return UserChallengeViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: CelebrityModel, listener: OnClickListener?) {
        with(itemView) {
            btJoinChallenge.visibility = View.GONE
            itemView.tvUserName.text = item.displayName?.decodeEmoji()
            itemView.useImage.loadImg(item.userImage)
            imvChallengers.loadImg(item.image)
            itemView.tvChallengersTitle.text = item.title?.decodeEmoji()
            tvTimer.text = SetDateTime.partTimeForFeedItem(item.created, context)
            tvDescription.text = item.description?.decodeEmoji()
            tvDuration.text = SetDateTime.timeChallengeDuration(item.startedAt, item.endedAt, context)
            tvViewDetails.setOnClickListener { listener?.onViewDetailClicked(item) }
            btJoin.setOnClickListener {
                if (item.isReachSubmissionLimit != null && item.isReachSubmissionLimit!!) {
                    listener?.canNotJoinChallenge(itemView.context.getString(R.string.toast_limited_join_challege))
                } else {
                    listener?.onJoinClicked(item)
                }

            }
            tvPrizes.text = item.prizeText?.decodeEmoji()
            item.isLike?.let {
                if (item.isLike!!) ivLike.setBackgroundResource(R.drawable.ic_heart_like_25dp_selected)
                else ivLike.setBackgroundResource(R.drawable.icon_like_off_tint)
            }
            ivLike.setOnClickListener { listener?.onLikeClicked(item, adapterPosition) }
            imgComment.setOnClickListener { listener?.onCommentClicked(item, adapterPosition) }
            moreOption.setOnClickListener { listener?.onOptionMenuClicked(item, adapterPosition) }
            imgShare.setOnClickListener { listener?.onShareClicked(item, adapterPosition) }
        }
    }

    interface OnClickListener {
        fun onJoinClicked(item: CelebrityModel)
        fun onViewDetailClicked(item: CelebrityModel)
        fun onLikeClicked(item: CelebrityModel, position: Int)
        fun canNotJoinChallenge(message: String)
        fun onCommentClicked(item: CelebrityModel, position: Int)
        fun onOptionMenuClicked(item: CelebrityModel, position: Int)
        fun onShareClicked(item: CelebrityModel, position: Int)
    }
}