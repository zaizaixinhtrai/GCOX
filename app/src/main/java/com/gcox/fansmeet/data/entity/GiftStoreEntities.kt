package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by thanhbc on 3/27/18.
 */
data class GiftStoreEntity(
    @SerializedName("Gems") val gems: Long,
    @SerializedName("GiftList") val giftItems: List<GiftStoreItemEntity>
)


data class GiftStoreItemEntity(
    @SerializedName("Id") var id: Int? = null,
    @SerializedName("Gem") var gem: Long? = null,
    @SerializedName("ReceivedStar") var receivedStar: Long?,
    @SerializedName("Image") var image: String ?,
    @SerializedName("Created") var created: String? = null,
    @SerializedName("Updated") var updated: String?
)