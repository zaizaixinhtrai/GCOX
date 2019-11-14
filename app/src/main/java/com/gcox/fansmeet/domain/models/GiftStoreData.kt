package com.gcox.fansmeet.domain.models

import com.gcox.fansmeet.features.profile.userprofile.gift.GiftItemModel

/**
 * Created by thanhbc on 3/27/18.
 */
data class GiftStoreModel(val gems: Long, val giftItems: List<GiftItemModel>)
