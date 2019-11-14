package com.gcox.fansmeet.features.challenges.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.challenges.ChallengeModel
import com.gcox.fansmeet.features.challenges.viewholders.ChallengeViewHolder


class ChallengeDelegate(private val listener: ChallengeViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<ChallengeModel, DisplayableItem, ChallengeViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is ChallengeModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): ChallengeViewHolder {
        return ChallengeViewHolder.create(parent, R.layout.challenge_item_layout)
    }

    override fun onBindViewHolder(item: ChallengeModel, viewHolder: ChallengeViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}