package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class AddCommentEntity(
    @field:SerializedName("CommentId")
    val commentId: Int? = 0,
    @field:SerializedName("Message")
    val message: String?,
    @field:SerializedName("Created")
    val created: String? = null,
    @field:SerializedName("CommentCount")
    val commentCount: Int? = 0
)
