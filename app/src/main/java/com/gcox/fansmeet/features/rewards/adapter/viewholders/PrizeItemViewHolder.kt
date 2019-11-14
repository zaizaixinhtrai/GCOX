package com.gcox.fansmeet.features.rewards.adapter.viewholders

import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.customview.hashtag.SpannableUtil
import com.gcox.fansmeet.features.prizelist.models.PrizeType
import com.gcox.fansmeet.features.rewards.models.BoxesModel
import kotlinx.android.synthetic.main.prize_item.view.*

class PrizeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): PrizeItemViewHolder {
            return PrizeItemViewHolder(parent.inflate(layout))
        }
    }

    fun bindTo(item: BoxesModel, listener: OnDisplayableItemClicked?) {
        with(itemView) {
            tvPrizeType.text = item.name
            tvCost.text = SpannableUtil.replaceSquareGemIcon(context, item.getPrizeCost())
            if (item.canPlay != null && item.canPlay!!) {
                btnPlay.setOnClickListener { listener?.onDisplayableItemClicked(it, item) }
                lnBackground.setBackgroundResource(R.drawable.gray_background_border)
                btnPlay.setBackgroundResource(R.drawable.top_bottom_right_conner_background)
                btnPlay.setTextColor(Color.parseColor("#ffffff"))
            } else {
                lnBackground.setBackgroundResource(R.drawable.reward_off_border)
                btnPlay.setBackgroundResource(R.drawable.reward_box_off_background)
                btnPlay.setTextColor(Color.parseColor("#777777"))
            }
        }
    }
}