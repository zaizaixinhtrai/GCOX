package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserPostViewHolder
import com.gcox.fansmeet.manager.VideosManager


class UserPostDelegate(
    private val listener: UserPostViewHolder.OnClickListener?,
    private val videoPlayerManager: VideosManager
) : AbsListItemAdapterDelegate<CelebrityModel, DisplayableItem, UserPostViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebrityModel && (item.postType == Constants.USER_POST_NORMAL || item.postType == Constants.CHALLENGE_SUBMISSION)
    }

    override fun onCreateViewHolder(parent: ViewGroup): UserPostViewHolder {
        return UserPostViewHolder.create(parent, R.layout.item_post_detail)
    }

    override fun onBindViewHolder(item: CelebrityModel, viewHolder: UserPostViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item, listener, videoPlayerManager)
    }
}