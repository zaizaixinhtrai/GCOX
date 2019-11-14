package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class BoxesTypeEntity(
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("OwnerId")
    val ownerId: Int? = 0,
    @field:SerializedName("Name")
    val name: String? = "",
    @field:SerializedName("Type")
    val type: Int? = 0
)