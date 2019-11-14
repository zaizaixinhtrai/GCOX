package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class StarEntity(
    @field:SerializedName("Histories")
    val historiesEntity: HistoriesEntity? ,
    @field:SerializedName("Balance")
    val balance: Long? = 0
)

data class HistoriesEntity(
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false,
    @field:SerializedName("Result")
    val result: List<StarsItemEntity>? = null
)

data class StarsItemEntity(
    @field:SerializedName("Title")
    val title: String?,
    @field:SerializedName("EntryId")
    val entryId: Int?,
    @field:SerializedName("Star")
    val star: Long?,
    @field:SerializedName("Updated")
    val updated: String?
)