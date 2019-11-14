package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class PrizeEntity(
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("Description")
    val description: String? = "",
    @field:SerializedName("Title")
    val title: String? = "",
    @field:SerializedName("Image")
    val image: String? = "",
    @field:SerializedName("Type")
    val type: Int? = 0,
    @field:SerializedName("WebUrl")
    val webUrl: String? = ""
)