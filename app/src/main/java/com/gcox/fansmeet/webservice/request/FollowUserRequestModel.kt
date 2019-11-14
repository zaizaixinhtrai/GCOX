package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FollowUserRequestModel (@SerializedName("FollowUserId")
                              @Expose
                              var userId: Int = 0)