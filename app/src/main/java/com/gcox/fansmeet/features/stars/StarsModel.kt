package com.gcox.fansmeet.features.stars

import com.gcox.fansmeet.core.adapter.DisplayableItem

data class StarResponse(
    val historiesResponse: HistoriesResponse?,
    val balance: Long? = 0
)

class HistoriesResponse(
    var nextId: Int? = 0,
    var isEnd: Boolean? = false,
    var result: List<StarsModel>? = null
)

class StarsModel(
    val title: String? ,
    val entryId: Int?,
    val star: Long? = 100,
    val updated: String?
) : DisplayableItem