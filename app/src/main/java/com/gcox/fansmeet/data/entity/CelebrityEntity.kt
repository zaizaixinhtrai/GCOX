package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class CelebrityEntity(
    @field:SerializedName("Result")
    val result: List<CelebrityListEntity>? = null,
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false
)

data class CelebrityListEntity(
    @field:SerializedName("UserId")
    val userId: Int? = 0,
    @field:SerializedName("UserName")
    val userName: String? = null,
    @field:SerializedName("DisplayName")
    val displayName: String? = null,
    @field:SerializedName("UserImage")
    val userImage: String? = null,
    @field:SerializedName("Description")
    val description: String? = null,
    @field:SerializedName("UserBannerImage")
    val userBannerImage: String? = null,
    @field:SerializedName("Id")
    val id: Int? = null,
    @field:SerializedName("Title")
    val title: String? = null,
    @field:SerializedName("MediaType")
    val mediaType: Int? = null,
    @field:SerializedName("Image")
    val image: String? = null,
    @field:SerializedName("Video")
    val video: String? = null,
    @field:SerializedName("CommentCount")
    val commentCount: Int? = null,
    @field:SerializedName("LikeCount")
    val likeCount: Int? = null,
    @field:SerializedName("StartedAt")
    val startedAt: String? = null,
    @field:SerializedName("EndedAt")
    val endedAt: String? = null,
    @field:SerializedName("Created")
    val created: String? = null,
    @field:SerializedName("Comments")
    val comments: List<CommentEntity>? = null,
    @field:SerializedName("HashTag")
    val hashTag: String? = null,
    @field:SerializedName("PrizeText")
    val prizeText: String? = null,
    @field:SerializedName("EntryCount")
    val entryCount: Int? = null,
    @field:SerializedName("IsJoined")
    val isJoined: Boolean? = null,
    @field:SerializedName("IsReachSubmissionLimit")
    val isReachSubmissionLimit: Boolean? = null,
    @field:SerializedName("PostType")
    val postType: Int? = null,
    @field:SerializedName("IsLike")
    val isLike: Boolean? = false,
    @field:SerializedName("IsReported")
    val isReport: Boolean? = false,
    @field:SerializedName("MaxSubmission")
    val maxSubmission: Int? = null,
    @field:SerializedName("SelfieImage")
    val selfieImage: String? = null,
    @field:SerializedName("WebUrl")
    val webPostUrl: String? = null

)

data class CommentEntity(
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("UserId")
    val userId: Int? = null,
    @field:SerializedName("DisplayName")
    val displayName: String? = null,
    @field:SerializedName("UserName")
    val userName: String? = null,
    @field:SerializedName("Message")
    val message: String? = null,
    @field:SerializedName("Gender")
    val gender: String? = null,
    @field:SerializedName("Image")
    val image: String? = null,
    @field:SerializedName("Created")
    val created: String? = null,
    @field:SerializedName("UserUpdated")
    val userUpdated: String? = null
)