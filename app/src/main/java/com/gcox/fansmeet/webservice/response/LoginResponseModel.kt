package com.gcox.fansmeet.webservice.response


import com.gcox.fansmeet.models.UserModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by User on 9/24/2015.
 */
class LoginResponseModel {

    @SerializedName("UserInfo")
    @Expose
    var userInfo: UserModel? = null
    @SerializedName("AccessToken")
    @Expose
    var accessToken: String? = null
    @SerializedName("RefreshToken")
    @Expose
    var refreshToken: String? = null
    @SerializedName("Expires")
    @Expose
    var expires: String? = null
    @SerializedName("LoginType")
    @Expose
    val loginType: Int = 0

    companion object {
        val LOGIN_FROM_FACEBOOK = 0
        val LOGIN_FROM_GOOGLE = 3
    }
}
