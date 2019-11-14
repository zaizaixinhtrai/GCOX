package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

data class SendGiftEntity(
    @SerializedName("SenderGems") val senderGems: Long?,
    @SerializedName("ReceiverStars") val receiverStars: Long?
)
