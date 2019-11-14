package com.gcox.fansmeet.features.home.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.home.CelebritiesMode
import com.gcox.fansmeet.features.home.viewholders.HomeViewHolder
import com.gcox.fansmeet.features.home.HomeModel

/**
 * Created by ngoc on 5/16/18.
 */

class HomeDelegate(private val listener: HomeViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<CelebritiesMode, DisplayableItem, HomeViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebritiesMode
    }

    override fun onCreateViewHolder(parent: ViewGroup): HomeViewHolder {
        return HomeViewHolder.create(parent, R.layout.live_show_item_multi)
    }

    override fun onBindViewHolder(item: CelebritiesMode, viewHolder: HomeViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}
