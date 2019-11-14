package com.gcox.fansmeet.features.loyalty

import com.gcox.fansmeet.core.adapter.DisplayableItem

data class LoyaltyResponse(
    val historiesEntity: LoyaltyHistoriesResponse?,
    val balance: Long? = 0
)

class LoyaltyHistoriesResponse(
    val nextId: Int? = 0,
    val isEnd: Boolean? = false,
    val result: List<LoyaltyModel>? = null
)

class LoyaltyModel(
    val displayName: String?,
    val userType: Int?,
    val loyalty: Long?,
    val timer:String?
) : DisplayableItem