package com.gcox.fansmeet.features.challenges

import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.UpdateableItem

class ChallengeModel(
    val id: Int? = 0,
    val hashTag: String?,
    val entryCount: Int?,
    val entries: List<Entries>? = null,
    val title: String?,
    val mediaType : Int?
) : UpdateableItem{
    override fun isSameItem(item: DisplayableItem?): Boolean {
        return item is ChallengeModel && this.id == item.id
    }

    override fun isSameContent(item: DisplayableItem?): Boolean {
        return item is ChallengeModel && this.hashCode() == item.hashCode()
    }
}

class Entries(
    val id: Int? = 0,
    val userId: Int? = 0,
    val userName: String?,
    val displayName: String?,
    val userImage: String?,
    val userBannerImage: String?,
    val starCount: Int? = 0,
    val image: String? = null,
    val mediaType : Int?
)