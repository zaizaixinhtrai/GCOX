package com.gcox.fansmeet.features.challengedetail.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesModel
import com.gcox.fansmeet.util.glide.BlurTransformation
import com.gcox.fansmeet.util.glide.GlideApp
import kotlinx.android.synthetic.main.join_challenge_entries_item.view.*

class ChallengeDetailEntriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): ChallengeDetailEntriesViewHolder {
            return ChallengeDetailEntriesViewHolder(parent.inflate(layout))
        }
    }

    fun bindTo(item: JoinChallengeEntriesModel?, listener: ChallengeDetailEntriesViewHolder.OnClickListener?) {
        with(itemView) {
            item?.let {
                var mediaImage = it.image
                if (item.mediaType == Constants.POST_TYPE_QUOTES) mediaImage = it.userImage!!
                GlideApp
                    .with(context)
                    .load(mediaImage)
                    .transform(BlurTransformation(context))
                    .error(R.drawable.user_image_default)
                    .into(blurImage)
                useImage.loadImg(mediaImage)
                tvStart.text = it.starCount.toString()
                blurImage.setOnClickListener { listener?.onEntriesClicked(item) }
            }
        }
    }

    interface OnClickListener {
        fun onEntriesClicked(item: JoinChallengeEntriesModel)
    }
}