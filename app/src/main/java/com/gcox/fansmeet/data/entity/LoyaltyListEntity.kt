package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class LoyaltyEntity(
    @field:SerializedName("Histories")
    val historiesEntity: LoyaltyHistoriesEntity? ,
    @field:SerializedName("Balance")
    val balance: Long? = 0
)

data class LoyaltyHistoriesEntity(
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false,
    @field:SerializedName("Result")
    val result: List<LoyaltyItemEntity>? = null
)

data class LoyaltyItemEntity(
    @field:SerializedName("DisplayName")
    val displayName: String?,
    @field:SerializedName("UserType")
    val userType: Int?,
    @field:SerializedName("Loyalty")
    val loyalty: Long?,
    @field:SerializedName("Updated")
    val timer: String?
)