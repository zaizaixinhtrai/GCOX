package com.gcox.fansmeet.features.rewards.models

import android.os.Parcelable
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.UpdateableItem
import com.gcox.fansmeet.features.prizelist.models.PrizeType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RewardItem(val user: RewardUser, val prizeItemImages: List<String>?) :
    Parcelable, UpdateableItem {

    override fun isSameItem(item: DisplayableItem?): Boolean {
        return item is RewardItem && item.user.ownerId == this.user.ownerId
    }

    override fun isSameContent(item: DisplayableItem?): Boolean {
        return item is RewardItem && item.user.ownerId == this.user.ownerId
    }
}

@Parcelize
data class RewardUser(
    val userName: String?,
    val userImage: String?,
    val displayName: String?,
    val ownerId: Int? = 0
) : Parcelable
