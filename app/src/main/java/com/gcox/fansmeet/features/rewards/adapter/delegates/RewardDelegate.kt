package com.gcox.fansmeet.features.rewards.adapter.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.rewards.adapter.viewholders.PrizeViewHolder
import com.gcox.fansmeet.features.rewards.adapter.viewholders.RewardViewHolder
import com.gcox.fansmeet.features.rewards.models.RewardItem

class RewardDelegate(private val listener: OnDisplayableItemClicked?) : AbsListItemAdapterDelegate<RewardItem, DisplayableItem, RewardViewHolder>() {
    override fun isForViewType(
        item: DisplayableItem,
        items: MutableList<DisplayableItem>,
        position: Int
    ) = item is RewardItem

    override fun onCreateViewHolder(parent: ViewGroup): RewardViewHolder {
        return RewardViewHolder.create(parent, R.layout.reward_item)
    }

    override fun onBindViewHolder(
        item: RewardItem,
        viewHolder: RewardViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bindTo(item,listener)
    }
}