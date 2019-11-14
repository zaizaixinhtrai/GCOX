package com.gcox.fansmeet.features.rewards.adapter

import com.gcox.fansmeet.common.DiffCallBaseUtils
import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.features.rewards.adapter.delegates.PrizeDelegate
import com.gcox.fansmeet.features.rewards.adapter.delegates.RewardDelegate

class PrizeListAdapter(
    diffCallback: BaseDiffCallback<*>? = DiffCallBaseUtils(),
    items: List<DisplayableItem>,
    listener: OnDisplayableItemClicked?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(PrizeDelegate(listener))
        setItems(items)
    }
}