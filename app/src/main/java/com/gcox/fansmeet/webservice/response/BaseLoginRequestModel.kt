package com.gcox.fansmeet.webservice.response

import com.gcox.fansmeet.common.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseLoginRequestModel {
    @SerializedName("UserName")
    @Expose
    var userName: String? = null
    @SerializedName("Email")
    @Expose
    var email: String? = null
    @SerializedName("DisplayName")
    @Expose
    var displayName: String? = null
    @SerializedName("Latitude")
    @Expose
    var latitude: Double = 0.toDouble()
    @SerializedName("Longitude")
    @Expose
    var longitude: Double = 0.toDouble()
    @SerializedName("DeviceUdid")
    @Expose
    var device_udid: String? = null
    @SerializedName("DeviceType")
    @Expose
    var deviceType = Constants.ANDROID_DEVICE_TYPE
    @SerializedName("Profile_Pic")
    @Expose
    var profilePic: String? = null
    @SerializedName("Gender")
    @Expose
    var gender: String? = null
    @SerializedName("DeviceToken")
    @Expose
    var deviceToken: String? = null
}
