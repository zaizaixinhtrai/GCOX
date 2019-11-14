package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName


data class RewardListEntity(
    @field:SerializedName("UserName")
    val userName: String? = "",
    @field:SerializedName("DisplayName")
    val displayName: String?,
    @field:SerializedName("UserImage")
    val userImage: String?,
    @field:SerializedName("UserBannerImage")
    val userBannerImage: String?,
    @field:SerializedName("PrizeItemImages")
    val prizeItemImages: List<String>?,
    @field:SerializedName("ownerId")
    val ownerId: Int? =0
)

