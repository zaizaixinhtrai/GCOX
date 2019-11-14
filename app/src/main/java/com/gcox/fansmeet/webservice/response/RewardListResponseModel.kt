package com.gcox.fansmeet.webservice.response

import com.gcox.fansmeet.data.entity.RewardListEntity
import com.google.gson.annotations.SerializedName

data class BoxListResponse (@field:SerializedName("Result")
                       val boxes: List<RewardListEntity>?,
                       @field:SerializedName("NextId")
                       val nextId: Int?,
                       @field:SerializedName("IsEnd")
                       val isEnded: Boolean?)
