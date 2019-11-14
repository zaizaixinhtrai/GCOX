package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.ChallengersViewHolder


class ChallengersDelegate(private val listener: ChallengersViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<CelebrityModel, DisplayableItem, ChallengersViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebrityModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): ChallengersViewHolder {
        return ChallengersViewHolder.create(parent, R.layout.challengers_item_layout)
    }

    override fun onBindViewHolder(item: CelebrityModel, viewHolder: ChallengersViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}