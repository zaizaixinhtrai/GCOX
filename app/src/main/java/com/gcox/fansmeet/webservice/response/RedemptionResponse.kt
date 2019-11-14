package com.gcox.fansmeet.webservice.response

import com.google.gson.annotations.SerializedName

class RedemptionResponse(
    @field:SerializedName("Gems")
    val gems: Long? = 0,
    @field:SerializedName("AddedGems")
    val addedGems: Long? = 0
)

