package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.SerializedName

/**
 * Created by User on 12/7/2016.
 */

class TwitterLoginRequestModel : BaseLoginRequestModel() {

    @SerializedName("TwitterId")
    var twitterId: String? = null
}
