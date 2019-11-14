package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

class CelebrityGridResponse(
    var result: List<GridModel>? = null,
    var nextId: Int? = 0,
    var isEnd: Boolean? = false,
    var isRefresh:Boolean?= false
)

class GridModel(
    var id: Int?,
    var mediaType: Int?,
    var image: String?,
    var postType: Int?
)
