package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.profile.userprofile.viewholders.UserChallengeViewHolder


class UserChallengeDelegate(private val listener: UserChallengeViewHolder.OnClickListener?) :
    AbsListItemAdapterDelegate<CelebrityModel, DisplayableItem, UserChallengeViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebrityModel && item.postType == Constants.POST_TYPE_CHALLENGE
    }

    override fun onCreateViewHolder(parent: ViewGroup): UserChallengeViewHolder {
        return UserChallengeViewHolder.create(parent, R.layout.user_profile_list_item_layout)
    }

    override fun onBindViewHolder(item: CelebrityModel, viewHolder: UserChallengeViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item, listener)
    }
}