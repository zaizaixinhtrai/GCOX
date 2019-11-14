package com.gcox.fansmeet.features.challengedetail.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailEntriesViewHolder
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesModel

class ChallengeDetailEntriesDelegate(private val listener: ChallengeDetailEntriesViewHolder.OnClickListener?) :
    AbsListItemAdapterDelegate<JoinChallengeEntriesModel, DisplayableItem, ChallengeDetailEntriesViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is JoinChallengeEntriesModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): ChallengeDetailEntriesViewHolder {
        return ChallengeDetailEntriesViewHolder.create(parent, R.layout.join_challenge_entries_item)
    }

    override fun onBindViewHolder(
        item: JoinChallengeEntriesModel,
        viewHolder: ChallengeDetailEntriesViewHolder,
        payloads: List<Any>
    ) {
        viewHolder.bindTo(item, listener)
    }
}