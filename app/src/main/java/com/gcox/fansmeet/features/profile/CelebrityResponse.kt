package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.os.Parcelable
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.models.ItemClassComments
import kotlinx.android.parcel.Parcelize

class CelebrityResponse(
    var result: List<CelebrityModel>? = null,
    var nextId: Int? = 0,
    var isEnd: Boolean? = false,
    var isRefresh: Boolean = false
)

@Parcelize
data class CelebrityModel(
    var userId: Int? = 0,
    var userName: String? = null,
    var displayName: String? = null,
    var userImage: String? = null,
    var description: String? = null,
    var userBannerImage: String? = null,
    var id: Int? = null,
    var title: String? = null,
    var mediaType: Int? = null,
    var image: String? = null,
    var video: String? = null,
    var commentCount: Int? = null,
    var likeCount: Int? = null,
    var startedAt: String? = null,
    var endedAt: String? = null,
    var created: String? = null,
    var comments: List<ItemClassComments>? = null,
    var hashTag: String? = null,
    var prizeText: String? = null,
    var entryCount: Int? = null,
    var isJoined: Boolean? = null,
    var isReachSubmissionLimit: Boolean? = null,
    var postType: Int? = null,
    var isLike: Boolean? = false,
    var isReport: Boolean? = false,
    var maxSubmission: Int?=1,
    var selfieImage: String? = "",
    var webPostUrl:String?=""
) : Parcelable, DisplayableItem

data class CelebrityComment(
    var id: Int? = 0,
    var userId: Int? = null,
    var displayName: String? = null,
    var userName: String? = null,
    var message: String? = null,
    var gender: String? = null,
    var image: String? = null,
    var created: String? = null,
    var userUpdated: String? = null
)