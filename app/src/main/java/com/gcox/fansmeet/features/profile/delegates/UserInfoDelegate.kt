package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserInfoViewHolder

/**
 * Created by Ngoc on 5/16/18.
 */

class UserInfoDelegate(private val listener: UserInfoViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<CelebrityProfileModel, DisplayableItem, UserInfoViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebrityProfileModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): UserInfoViewHolder {
        return UserInfoViewHolder.create(parent, R.layout.user_info_item_layout)
    }

    override fun onBindViewHolder(item: CelebrityProfileModel, viewHolder: UserInfoViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}
