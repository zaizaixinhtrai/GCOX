package com.gcox.fansmeet.features.refill

import com.google.gson.annotations.SerializedName

data class GemResponse(
    @field:SerializedName("Gems")
    val gems:Long?=0,
    @field:SerializedName("ProductList")
    val result: List<RefillListItem>? = null
)