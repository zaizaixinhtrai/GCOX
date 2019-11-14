package com.gcox.fansmeet.features.loyalty.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.loyalty.LoyaltyModel
import com.gcox.fansmeet.features.loyalty.viewholders.LoyaltyViewHolder
import com.gcox.fansmeet.features.stars.StarsModel
import com.gcox.fansmeet.features.stars.viewholders.StarsViewHolder


class LoyaltyDelegate(private val listener: LoyaltyViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<LoyaltyModel, DisplayableItem, LoyaltyViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is LoyaltyModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): LoyaltyViewHolder {
        return LoyaltyViewHolder.create(parent, R.layout.loyalty_row_layout)
    }

    override fun onBindViewHolder(item: LoyaltyModel, viewHolder: LoyaltyViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}