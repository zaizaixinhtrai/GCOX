package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GoogleLoginRequestModel : BaseLoginRequestModel() {
    @SerializedName("GoogleId")
    @Expose
    var googleId: String? = null
}
