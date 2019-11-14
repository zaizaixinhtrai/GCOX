package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName


data class TopFansEntity(
    @field:SerializedName("TotalGiftCount")
    val totalGiftCount: Long? = 0,
    @field:SerializedName("Fans")
    val topFansResponse: TopFansResponse?
)

data class TopFansResponse(
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false,
    @field:SerializedName("Result")
    val result: List<TopFanEntity>? = null
)

data class TopFanEntity(
    @field:SerializedName("UserId")
    val userId: Int? = 0,
    @field:SerializedName("UserName")
    val userName: String?,
    @field:SerializedName("DisplayName")
    val displayName: String?,
    @field:SerializedName("UserImage")
    val userImage: String?,
    @field:SerializedName("UserBannerImage")
    val userBannerImage: String?,
    @field:SerializedName("GiftCount")
    val giftCount: Int? = 0
)