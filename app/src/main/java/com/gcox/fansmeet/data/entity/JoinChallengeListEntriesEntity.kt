package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class ChallengeListEntriesEntityResponse(
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false,
    @field:SerializedName("Result")
    val result: List<ChallengeEntriesModel>? = null

)

data class ChallengeEntriesModel(
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("HashTag")
    val hashTag: String?,
    @field:SerializedName("EntryCount")
    val entryCount: Int?,
    @field:SerializedName("Entries")
    val entries: List<Entries>? = null,
    @field:SerializedName("Title")
    val title:String?= null,
    @field:SerializedName("MediaType")
    val mediaType: Int? = 0
)

data class Entries(
    @field:SerializedName("Id")
    val id: Int? = 0,
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
    @field:SerializedName("StarCount")
    val starCount: Int? = 0,
    @field:SerializedName("Image")
    val image:String?=null,
    @field:SerializedName("MediaType")
    val mediaType: Int? = 0

)