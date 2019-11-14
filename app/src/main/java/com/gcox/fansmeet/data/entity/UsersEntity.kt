package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by thanhbc on 5/18/18.
 */
data class UsersEntity(
    @field:SerializedName("Result")
    val result: List<CelebritiesEntity>? = null,
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false
)

data class CelebritiesEntity(
    @field:SerializedName("UserId")
    val userId: Int? = 0,
    @field:SerializedName("UserName")
    val userName: String? = null,
    @field:SerializedName("DisplayName")
    val displayName: String? = null,
    @field:SerializedName("UserImage")
    val userImage: String? = null,
    @field:SerializedName("BannerImage")
    val bannerImage: String? = null,
    @field:SerializedName("Gender")
    val gender: String? = null,
    @field:SerializedName("About")
    val about: String? = null,
    @field:SerializedName("Description")
    val description: String? = null,
    @field:SerializedName("IsFollow")
    val isFollow: Boolean? = false,
    @field:SerializedName("OnlyShowBannerImage")
    val onlyShowBannerImage: Int? = 0,
    @field:SerializedName("IsClickable")
    val isClickable: Int?

)


