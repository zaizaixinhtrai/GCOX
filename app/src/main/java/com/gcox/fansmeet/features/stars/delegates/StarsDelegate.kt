package com.gcox.fansmeet.features.stars.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.stars.StarsModel
import com.gcox.fansmeet.features.stars.viewholders.StarsViewHolder


class StarsDelegate(private val listener: StarsViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<StarsModel, DisplayableItem, StarsViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is StarsModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): StarsViewHolder {
        return StarsViewHolder.create(parent, R.layout.stars_row_layout)
    }

    override fun onBindViewHolder(item: StarsModel, viewHolder: StarsViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}