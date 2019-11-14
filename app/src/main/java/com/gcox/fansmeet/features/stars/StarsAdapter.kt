package com.gcox.fansmeet.features.stars

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.challenges.delegates.ChallengeDelegate
import com.gcox.fansmeet.features.stars.delegates.StarsDelegate
import com.gcox.fansmeet.features.stars.viewholders.StarsViewHolder

/**
 * Created by ngoc on 5/16/18.
 */

class StarsAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>,
    val listener: StarsViewHolder.OnClickListener?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(StarsDelegate(listener))
        setItems(items)
    }
}
