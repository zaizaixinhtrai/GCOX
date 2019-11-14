package com.gcox.fansmeet.features.challenges.viewholders

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.appster.extensions.toHashTag
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.challenges.ChallengeModel
import com.gcox.fansmeet.features.challenges.Entries
import com.gcox.fansmeet.util.glide.BlurTransformation
import com.gcox.fansmeet.util.glide.GlideApp
import kotlinx.android.synthetic.main.challenge_item_layout.view.*


class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): ChallengeViewHolder {
            return ChallengeViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: ChallengeModel, listener: OnClickListener?) {
        with(itemView) {
            fmArrow.setOnClickListener { listener?.onViewEntriesClicked(item) }
            llChallengeName.setOnClickListener { fmArrow.performClick() }
            tvHashTag.text = item.title?.toHashTag()
            if (item.entryCount!! >= 0)
                tvEntryCount.text = item.entryCount.toString()
            if (item.entries?.isNotEmpty()!!) {
                if (item.entries.size == 1) {
                    fm2.visibility = View.INVISIBLE
                    fm3.visibility = View.INVISIBLE
                    tvStart1.visibility = View.VISIBLE
                    tvStart3.visibility = View.INVISIBLE
                    tvStart2.visibility = View.INVISIBLE
                } else if (item.entries.size == 2) {
                    fm3.visibility = View.INVISIBLE
                    tvStart1.visibility = View.VISIBLE
                    tvStart2.visibility = View.VISIBLE
                    tvStart3.visibility = View.INVISIBLE
                } else {
                    fm2.visibility = View.VISIBLE
                    fm3.visibility = View.VISIBLE
                    fm1.visibility = View.VISIBLE
                    tvStart1.visibility = View.VISIBLE
                    tvStart3.visibility = View.VISIBLE
                    tvStart2.visibility = View.VISIBLE
                }

                for (i in 0 until item.entries.size) {
                    val entries = item.entries[i]
                    when (i) {
                        0 -> {
                            var image = entries.image!!
                            if (entries.mediaType == Constants.POST_TYPE_QUOTES) image = entries.userImage!!
                            blurImage(context, blurImage1, image)
                            useImage1.loadImg(image)
                            tvStart1.text = entries.starCount.toString()
                            blurImage1.setOnClickListener { listener?.onUserImageClicked(entries) }
                        }
                        1 -> {
                            var image = entries.image!!
                            if (entries.mediaType == Constants.POST_TYPE_QUOTES) image = entries.userImage!!
                            blurImage(context, blurImage2, image)
                            useImage2.loadImg(image)
                            tvStart2.text = entries.starCount.toString()
                            blurImage2.setOnClickListener { listener?.onUserImageClicked(entries) }
                        }
                        2 -> {
                            var image = entries.image!!
                            if (entries.mediaType == Constants.POST_TYPE_QUOTES) image = entries.userImage!!
                            blurImage(context, blurImage3, image)
                            useImage3.loadImg(image)
                            tvStart3.text = entries.starCount.toString()
                            blurImage3.setOnClickListener { listener?.onUserImageClicked(entries) }
                        }
                    }
                }
            } else {
                tvStart1.visibility = View.INVISIBLE
                tvStart3.visibility = View.INVISIBLE
                tvStart2.visibility = View.INVISIBLE
            }

        }
    }

    private fun blurImage(context: Context, imageView: ImageView, userImageLink: String) {
        GlideApp
            .with(context)
            .load(userImageLink)
            .transform(BlurTransformation(context))
            .error(R.drawable.user_image_default)
            .into(imageView)
    }


    interface OnClickListener {
        fun onUserImageClicked(item: Entries)
        fun onViewEntriesClicked(item: ChallengeModel)
    }
}