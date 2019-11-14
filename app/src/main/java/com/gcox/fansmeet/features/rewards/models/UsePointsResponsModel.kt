package com.gcox.fansmeet.features.rewards.models

import com.google.gson.annotations.SerializedName

class UsePointsResponsModel(
    @SerializedName("Id")
    val id: Int?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("ItemImage")
    val image: String?,
    @SerializedName("ItemType")
    val type: Int?,
    @SerializedName("ItemName")
    val itemName:String?
)