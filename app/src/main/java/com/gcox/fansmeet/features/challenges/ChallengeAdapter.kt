package com.gcox.fansmeet.features.challenges

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.challenges.delegates.ChallengeDelegate
import com.gcox.fansmeet.features.challenges.viewholders.ChallengeViewHolder

/**
 * Created by ngoc on 5/16/18.
 */

class ChallengeAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>,
    val listener: ChallengeViewHolder.OnClickListener?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(ChallengeDelegate(listener))
        setItems(items)
    }
}
