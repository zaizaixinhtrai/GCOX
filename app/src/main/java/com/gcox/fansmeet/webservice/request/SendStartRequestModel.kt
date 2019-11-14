package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendStartRequestModel(
    @SerializedName("GiftId")
    @Expose
    val star: Long
)