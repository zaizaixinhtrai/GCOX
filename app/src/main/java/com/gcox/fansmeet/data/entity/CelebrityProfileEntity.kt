package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class CelebrityProfileEntity(
    @field:SerializedName("FollowersCount")
    val followersCount: Long? = 0,
    @field:SerializedName("ChallengeCount")
    val challengeCount: Long? = 0,
    @field:SerializedName("GiftReceivedCount")
    val giftReceivedCount: Long? = 0,
    @field:SerializedName("UserId")
    val userId: Int? = 0,
    @field:SerializedName("UserName")
    val userName: String?,
    @field:SerializedName("DisplayName")
    val displayName: String? ,
    @field:SerializedName("UserImage")
    val userImage: String? ,
    @field:SerializedName("BannerImage")
    val bannerImage: String?,
    @field:SerializedName("Gender")
    val gender: String? ,
    @field:SerializedName("About")
    val about: String?,
    @field:SerializedName("Description")
    val description: String? ,
    @field:SerializedName("IsFollow")
    val isFollow: Boolean?,
    @field:SerializedName("Type")
    val type: Int? = 0,
    @field:SerializedName("FollowingCount")
    val followingCount: Long? = 0
)