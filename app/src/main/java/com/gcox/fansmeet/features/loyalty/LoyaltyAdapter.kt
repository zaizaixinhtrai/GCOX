package com.gcox.fansmeet.features.loyalty

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.loyalty.delegates.LoyaltyDelegate
import com.gcox.fansmeet.features.loyalty.viewholders.LoyaltyViewHolder

/**
 * Created by ngoc on 5/16/18.
 */

class LoyaltyAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>,
    val listener: LoyaltyViewHolder.OnClickListener?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(LoyaltyDelegate(listener))
        setItems(items)
    }
}
