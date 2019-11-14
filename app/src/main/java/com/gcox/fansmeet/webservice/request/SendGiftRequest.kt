package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.SerializedName

data class SendGiftRequest (@SerializedName("GiftId") val giftId: Int,
                            @SerializedName("ReceiverId") val receiverId: Int )