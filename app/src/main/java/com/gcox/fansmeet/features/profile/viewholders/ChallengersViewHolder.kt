package com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.util.SetDateTime
import com.gcox.fansmeet.util.Utils
import kotlinx.android.synthetic.main.challenge_info_layout.view.*
import kotlinx.android.synthetic.main.challengers_item_layout.view.*
import kotlinx.android.synthetic.main.feed_header_view.view.*
import kotlinx.android.synthetic.main.view_action_like_comment.*
import kotlinx.android.synthetic.main.view_action_like_comment.view.*


class ChallengersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): ChallengersViewHolder {
            return ChallengersViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: CelebrityModel, listener: OnClickListener?) {
        with(itemView) {
            btJoin.visibility = View.GONE
            tvViewDetails.visibility = View.GONE
            itemView.tvChallengers.visibility = View.GONE
            itemView.tvUserName.text = item.displayName
            itemView.useImage.loadImg(item.userImage)
            imvChallengers.loadImg(item.image)
            itemView.tvChallengersTitle.text = item.title
            if (item.commentCount!! > Constants.NUMBER_COMMENT_SHOW) {
                itemView.tvMoreComments.text =
                    context.getString(R.string.view_all_for, item.commentCount.toString())
            } else {
                itemView.tvMoreComments.visibility = View.GONE
            }

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
                    likeCountString =
                        String.format(context.getString(R.string.like_count), item.likeCount)
                }

                tvLikesCount.text = likeCountString

            }
            tvTimer.text = SetDateTime.partTimeForFeedItem(item.created, context)
            tvDescription.text = item.description
            tvDuration.text =
                SetDateTime.timeChallengeDuration(item.created, item.startedAt, context)
//            tvHashTag.text = item.hashTag?.toHashTag()
            itemView.btJoinChallenge.setOnClickListener { listener?.onJoinChallengeClicked(item) }
            itemView.imgShare.setOnClickListener { listener?.onShareClicked(item, adapterPosition) }
            tvPrizes.text = item.prizeText
        }

    }

    interface OnClickListener {
        fun onShareClicked(item: CelebrityModel, position: Int)
        fun onImageClicked(item: CelebrityModel)
        fun onJoinChallengeClicked(item: CelebrityModel)
    }
}