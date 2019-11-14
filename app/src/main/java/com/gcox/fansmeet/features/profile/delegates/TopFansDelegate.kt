package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.profile.FansModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.TopFansViewHolder

class TopFansDelegate(private val listener: TopFansViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<FansModel, DisplayableItem, TopFansViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is FansModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): TopFansViewHolder {
        return TopFansViewHolder.create(parent, R.layout.top_fans_item_layout)
    }

    override fun onBindViewHolder(item: FansModel, viewHolder: TopFansViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}