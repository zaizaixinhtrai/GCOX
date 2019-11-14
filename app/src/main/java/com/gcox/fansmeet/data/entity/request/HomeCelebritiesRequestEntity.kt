package com.gcox.fansmeet.data.entity.request

import com.google.gson.annotations.SerializedName

class HomeCelebritiesRequestEntity(@field:SerializedName("NextId")
                                   val nextId: Int,
                                   @field:SerializedName("Limit")
                                   val limit: Int,
                                   val type:Int)