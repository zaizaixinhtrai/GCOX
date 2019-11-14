package com.gcox.fansmeet.features.rewards.adapter.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.prizelist.models.Prize
import kotlinx.android.synthetic.main.fragment_prize.view.*

class PrizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): PrizeViewHolder {
            return PrizeViewHolder(parent.inflate(layout))
        }
    }

    fun bindTo(item: Prize, listener: OnDisplayableItemClicked?) {
        with(itemView) {
            ivPrizeImg.loadImg(item.prizeImg)
            tvPrizeDesc.text = item.title
            tvViewDetail.setOnClickListener { listener?.onDisplayableItemClicked(it, item) }
        }
    }
}