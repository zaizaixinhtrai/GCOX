package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FollowEntity(
    @PrimaryKey @SerializedName("UserId") var userId: Int? = 0,
    @SerializedName("UserName")
    var userName: String?="",
    @SerializedName("DisplayName")
    var displayName: String? = null,
    @SerializedName("UserImage")
    var userImage: String?="",
    @SerializedName("BannerImage")
    var bannerImage: String?="",
    @SerializedName("Type")
    var type: Int?=0,
    @SerializedName("Gender")
    var gender: String?="",
    @SerializedName("About")
    var about: String?="",
    @SerializedName("Description")
    var description: String?="",
    @SerializedName("IsFollow")
    var isFollow: Boolean? = false
): RealmObject()