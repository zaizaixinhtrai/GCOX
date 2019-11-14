package com.gcox.fansmeet.features.home

import com.gcox.fansmeet.core.adapter.DisplayableItem

class HomeModel(
    var result: List<CelebritiesMode>? = null,
    var nextId: Int? =0,
    var isEnd: Boolean? = false
) : DisplayableItem

class CelebritiesMode : DisplayableItem {
    var userId: Int? = 0
    var userName: String? = null
    var displayName: String? = null
    var userImage: String? = null
    var bannerImage: String? = null
    var gender: String? = null
    var about: String? = null
    var description: String? = null
    var isFollow: Boolean? = false
    var onlyShowBannerImage: Int? = 0
    var isClickable : Int? =0
}