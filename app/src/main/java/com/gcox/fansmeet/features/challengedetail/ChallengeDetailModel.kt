package com.gcox.fansmeet.features.joinchallenge

import com.gcox.fansmeet.core.adapter.DisplayableItem

data class JoinChallengeEntriesResponse(
    val nextId: Int? = 0,
    val isEnd: Boolean? = false,
    val result: List<JoinChallengeEntriesModel>? = null
)

data class JoinChallengeEntriesModel(
    val id: Int? = 0,
    var userId: Int? = 0,
    var userName: String?,
    var displayName: String?,
    var userImage: String?,
    var userBannerImage: String?,
    var starCount: Int? = 0,
    var image: String?,
    var mediaType: Int?
) : DisplayableItem