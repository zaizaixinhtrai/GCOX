package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class ContestantEntriesEntity(
    @field:SerializedName("DisplayName")
    val displayName: String? ,
    @field:SerializedName("UserImage")
    val userImage: String? ,
    @field:SerializedName("UserBannerImage")
    val userBannerImage: String? ,
    @field:SerializedName("IsLike")
    val isLike: Boolean? ,
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("ChallengeId")
    val challengeId: Int? = 0,
    @field:SerializedName("UserId")
    val userId: Int? = 0,
    @field:SerializedName("Caption")
    val caption: String?,
    @field:SerializedName("Image")
    val image: String? ,
    @field:SerializedName("Video")
    val video: String?,
    @field:SerializedName("StarCount")
    val starCount: Int? = 0,
    @field:SerializedName("CommentCount")
    val commentCount: Int? = 0,
    @field:SerializedName("LikeCount")
    val likeCount: Int? = 0,
    @field:SerializedName("TagUsers")
    val tagUsers: String?,
    @field:SerializedName("Created")
    val created: String?,
    @field:SerializedName("IsReported")
    val isReported: Boolean?,
    @field:SerializedName("MediaType")
    val mediaType: Int? = 0,
    @field:SerializedName("IsFollowed")
    val isFollowed: Boolean?,
    @field:SerializedName("SelfieImage")
    val selfieImage: String?,
    @field:SerializedName("WebUrl")
    val webPostUrl: String?,
    @field:SerializedName("UserName")
    val userName: String?
)