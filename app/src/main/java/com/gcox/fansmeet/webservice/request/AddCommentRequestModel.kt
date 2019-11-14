package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddCommentRequestModel(
    @SerializedName("PostId")
    @Expose
    var postId: Int? = null,
    @SerializedName("Message")
    @Expose
    var message: String? = null,
    @SerializedName("TagUsers")
    @Expose
    var tagUsers:String? = null
)