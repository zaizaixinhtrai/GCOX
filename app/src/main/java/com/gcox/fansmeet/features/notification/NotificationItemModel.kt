package com.gcox.fansmeet.features.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by User on 6/24/2016.
 */
class NotificationItemModel {

    @SerializedName("Id")
    @Expose
    val id: Int? = 0
    @SerializedName("notificationType")
    @Expose
    val notificationType: String? = ""
    @SerializedName("Title")
    @Expose
    val title: String? = ""
    @SerializedName("Message")
    @Expose
    val message: String? = ""
    @SerializedName("Created")
    @Expose
    val created: String? = ""
    @SerializedName("SenderId")
    @Expose
    val senderId: String? = ""
    @SerializedName("SenderName")
    @Expose
    val senderName: String? = ""
    @SerializedName("SenderDisplayName")
    @Expose
    val senderDisplayName: String? = ""
    @SerializedName("SenderImage")
    @Expose
    val senderImage: String? = ""
    @SerializedName("SenderBannerImage")
    @Expose
    val senderBannerImage: String? = ""

}
