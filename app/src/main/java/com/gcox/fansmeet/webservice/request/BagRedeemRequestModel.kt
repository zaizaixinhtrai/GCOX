package com.gcox.fansmeet.webservice.request

import com.google.gson.annotations.SerializedName

data class BagRedeemRequestModel (@field:SerializedName("Name")
                                  val name: String? = "",
                                  @field:SerializedName("Email")
                                  val email: String? = "")