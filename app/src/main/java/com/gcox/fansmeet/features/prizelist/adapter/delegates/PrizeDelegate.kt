package com.gcox.fansmeet.features.rewards.adapter.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.rewards.adapter.viewholders.PrizeViewHolder
import com.gcox.fansmeet.features.rewards.models.RewardItem

class PrizeDelegate(private val listener: OnDisplayableItemClicked?) : AbsListItemAdapterDelegate<Prize, DisplayableItem, PrizeViewHolder>() {
    override fun isForViewType(
        item: DisplayableItem,
        items: MutableList<DisplayableItem>,
        position: Int
    ) = item is Prize

    override fun onCreateViewHolder(parent: ViewGroup): PrizeViewHolder {
        return PrizeViewHolder.create(parent, R.layout.fragment_prize)
    }

    override fun onBindViewHolder(
        item: Prize,
        viewHolder: PrizeViewHolder,
        payloads: MutableList<Any>
    ) {
      viewHolder.bindTo(item,listener)
    }
}