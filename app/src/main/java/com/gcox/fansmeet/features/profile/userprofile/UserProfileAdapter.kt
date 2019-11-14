package com.gcox.fansmeet.features.profile.userprofile

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.UserPostDelegate
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.UserChallengeDelegate
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserPostViewHolder
import com.gcox.fansmeet.features.profile.userprofile.viewholders.UserChallengeViewHolder
import com.gcox.fansmeet.manager.VideosManager

/**
 * Created by ngoc on 5/16/18.
 */

class UserProfileAdapter(
    diffCallback: BaseDiffCallback<*>?,
    items: List<DisplayableItem>,
    val listener: UserChallengeViewHolder.OnClickListener?,
    postListener: UserPostViewHolder.OnClickListener?,
    videoPlayerManager: VideosManager
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(UserChallengeDelegate(listener))
        this.delegatesManager.addDelegate(UserPostDelegate(postListener, videoPlayerManager))
        setItems(items)
    }
}
