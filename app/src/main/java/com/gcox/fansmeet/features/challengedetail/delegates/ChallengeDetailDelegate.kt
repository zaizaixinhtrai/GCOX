package com.gcox.fansmeet.features.challengedetail.delegates

import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.challengedetail.viewholders.ChallengeDetailViewHolder
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel


class ChallengeDetailDelegate(private val listener: ChallengeDetailViewHolder.OnClickListener?) : AbsListItemAdapterDelegate<CelebrityModel, DisplayableItem, ChallengeDetailViewHolder>() {

    override fun isForViewType(item: DisplayableItem, items: List<DisplayableItem>, position: Int): Boolean {
        return item is CelebrityModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): ChallengeDetailViewHolder {
        return ChallengeDetailViewHolder.create(parent, R.layout.join_challenger_item)
    }

    override fun onBindViewHolder(item: CelebrityModel, viewHolder: ChallengeDetailViewHolder, payloads: List<Any>) {
        viewHolder.bindTo(item,listener)
    }
}