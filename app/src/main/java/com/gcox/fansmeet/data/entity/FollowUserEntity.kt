package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class FollowUserEntity(
    @field:SerializedName("FollowerCount")
    val userFollowerCount: Long? = 0,
    @field:SerializedName("FollowingCount")
    val meFollowingCount: Long? = 0
)