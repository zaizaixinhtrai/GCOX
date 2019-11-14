package com.gcox.fansmeet.features.rewards.adapter.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.prizelist.models.PrizeType
import com.gcox.fansmeet.features.rewards.adapter.viewholders.PrizeItemViewHolder
import com.gcox.fansmeet.features.rewards.models.BoxesModel

class PrizeItemDelegate(private val listener: OnDisplayableItemClicked?) :
    AbsListItemAdapterDelegate<BoxesModel, DisplayableItem, PrizeItemViewHolder>() {
    override fun isForViewType(
        item: DisplayableItem,
        items: MutableList<DisplayableItem>,
        position: Int
    ) = item is BoxesModel

    override fun onCreateViewHolder(parent: ViewGroup): PrizeItemViewHolder {
        return PrizeItemViewHolder.create(parent, R.layout.prize_item)
    }

    override fun onBindViewHolder(
        item: BoxesModel,
        viewHolder: PrizeItemViewHolder,
        payloads: MutableList<Any>
    ) {
        viewHolder.bindTo(item, listener)
    }
}