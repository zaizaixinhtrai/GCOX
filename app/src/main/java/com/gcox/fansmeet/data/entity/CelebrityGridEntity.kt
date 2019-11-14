package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class CelebrityGridEntity(
    @field:SerializedName("Result")
    val result: List<GridEntity>? = null,
    @field:SerializedName("NextId")
    val nextId: Int? = 0,
    @field:SerializedName("IsEnd")
    val isEnd: Boolean? = false
)

data class GridEntity(
    @field:SerializedName("Id")
    val id: Int? ,
    @field:SerializedName("MediaType")
    val mediaType: Int? ,
    @field:SerializedName("Image")
    val image: String?,
    @field:SerializedName("PostType")
    val postType: Int?
)
