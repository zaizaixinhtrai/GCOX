package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class CommentsListEntity(
    @field:SerializedName("CommentId")
    val commentId: Int? = 0,
    @field:SerializedName("UserId")
    val userId: Int?,
    @field:SerializedName("DisplayName")
    val displayName: String? = null,
    @field:SerializedName("UserName")
    val userName: String?,
    @field:SerializedName("Message")
    val message: String?,
    @field:SerializedName("Gender")
    val gender: String?,
    @field:SerializedName("UserImage")
    val userImage: String?,
    @field:SerializedName("Created")
    val created: String?,
    @field:SerializedName("Timestamp")
    val timestamp: String?
)
