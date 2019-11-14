package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.SerializedName

class LoginFacebookRequestModel : BaseLoginRequestModel() {
    @SerializedName("FbId")
    var fbId: String? = null
}
