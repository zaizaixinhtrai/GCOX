package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import com.gcox.fansmeet.core.adapter.DisplayableItem

class CelebrityProfileModel (var followersCount: Long? = 0,
                             var challengeCount: Long? = 0,
                             var giftReceivedCount: Long? = 0,
                             var userId: Int? = 0,
                             var userName: String?,
                             var displayName: String?,
                             var userImage: String?,
                             var bannerImage: String?,
                             var gender: String?,
                             var about: String?,
                             var description: String?,
                             var isFollow: Boolean?,
                             var type: Int? = 0,
                             var followingCount: Long? = 0):DisplayableItem