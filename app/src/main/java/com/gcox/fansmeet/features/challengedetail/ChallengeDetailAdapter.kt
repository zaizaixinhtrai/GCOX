package com.gcox.fansmeet.features.challengedetail

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.challengedetail.delegates.ChallengeDetailDelegate
import com.gcox.fansmeet.features.challengedetail.delegates.ChallengeDetailEntriesDelegate
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailEntriesViewHolder
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailViewHolder


/**
 * Created by Ngoc on 5/16/18.
 */

class ChallengeDetailAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>, challengersListener: ChallengeDetailViewHolder.OnClickListener?, entriesListener: ChallengeDetailEntriesViewHolder.OnClickListener?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(ChallengeDetailEntriesDelegate(entriesListener))
        this.delegatesManager.addDelegate(ChallengeDetailDelegate(challengersListener))

        setItems(items)
    }
}
