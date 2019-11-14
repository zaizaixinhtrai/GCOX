package com.gcox.fansmeet.features.profile

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.ChallengersDelegate
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.TopFansDelegate
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.UserInfoDelegate
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.ChallengersViewHolder
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.TopFansViewHolder
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserInfoViewHolder

/**
 * Created by thanhbc on 5/16/18.
 */

class CelebrityProfileAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>, val listener: UserInfoViewHolder.OnClickListener?
    , challengersListener: ChallengersViewHolder.OnClickListener?, topFansListener: TopFansViewHolder.OnClickListener
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(UserInfoDelegate(listener))
        this.delegatesManager.addDelegate(TopFansDelegate(topFansListener))
        this.delegatesManager.addDelegate(ChallengersDelegate(challengersListener))

        setItems(items)
    }
}
