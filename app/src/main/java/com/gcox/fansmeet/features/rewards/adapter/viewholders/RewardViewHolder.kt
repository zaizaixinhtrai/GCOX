package com.gcox.fansmeet.features.rewards.adapter.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.rewards.models.RewardItem
import kotlinx.android.synthetic.main.reward_item.view.*

class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): RewardViewHolder {
            return RewardViewHolder(parent.inflate(layout))
        }
    }

    fun bindTo(item: RewardItem, listener: OnDisplayableItemClicked?) {
        with(itemView) {
            val celeb = item.user
            tvCelebName.text = celeb.displayName
            ivCelebImage.loadImg(celeb.userImage)
            item.prizeItemImages?.forEach {
                val prize = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.prize_img_item, llPrizeContainer, false)
                val v = prize.findViewById<ImageView>(R.id.ivPrizeImg)
                v.loadImg(it)

                llPrizeContainer.addView(v)
            }
            tvPrizeList.setOnClickListener {
                listener?.onDisplayableItemClicked(it, item)
            }
            btnPlay.setOnClickListener { listener?.onDisplayableItemClicked(it, item) }
        }
    }

}