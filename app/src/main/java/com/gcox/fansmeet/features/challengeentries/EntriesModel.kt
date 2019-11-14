package com.gcox.fansmeet.features.challengeentries

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class EntriesModel(
    val displayName: String?,
    val userImage: String?,
    val userBannerImage: String?,
    var isLike: Boolean?,
    val id: Int? = 0,
    val challengeId: Int? = 0,
    val userId: Int? = 0,
    var caption: String?,
    var image: String?,
    var video: String?,
    val starCount: Int? = 0,
    var commentCount: Int? = 0,
    var likeCount: Int? = 0,
    val tagUsers: String?,
    val created: String?,
    var isFollow: Boolean? = false,
    var isReport: Boolean? = false,
    var mediaType: Int?,
    var selfieImage:String? = "",
    var webPostUrl:String?,
    val userName :String?

): Parcelable