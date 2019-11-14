package com.gcox.fansmeet.models

import android.os.Parcel
import android.os.Parcelable
import com.appster.extensions.parcelableCreator
import com.gcox.fansmeet.BuildConfig
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel : Parcelable, DisplayableItem {
    @SerializedName("UserId")
    var userId = 0
    @SerializedName("RoleId")
    var roleId: String? = null
    @SerializedName("ReferralId")
    var referralId: String? = null
    @SerializedName("UserName")
    var userName: String? = null
    @SerializedName("DisplayName")
    var displayName: String? = null
    @SerializedName("Email")
    var email: String? = null
    @SerializedName("EmailVerified")
    var emailVerified: Int = 0
    @SerializedName("EmailToken")
    var emailToken: String? = null
    @SerializedName("UserImage")
    var userImage: String? = null
    @SerializedName("Gender")
    var gender: String? = null
    @SerializedName("DoB")
    var doB: String? = null
    @SerializedName("Nationality")
    var nationality: String? = null
    @SerializedName("Notification")
    var notification: Int = 0
    @SerializedName("Searchable")
    var searchable: Int = 0
    @SerializedName("NotificationSound")
    var notificationSound: Int = 0
    @SerializedName("LiveNotification")
    @Expose
    var liveNotification: Int = 0
    @SerializedName("Status")
    var status: Int = 0
    @SerializedName("Created")
    var created: String? = null
    @SerializedName("FollowerCount")
    var followerCount: Long = 0
    @SerializedName("FollowingCount")
    var followingCount: Long = 0
    @SerializedName("IsFollow")
    var isFollow: Int = 0
    @SerializedName("UnreadMessageCount")
    var unreadMessageCount: Int = 0
    @SerializedName("UnreadNotificationCount")
    var unreadNotificationCount: Int = 0
    @SerializedName("LoginLogoutStatus")
    var loginLogoutStatus: Int = 0
    @SerializedName("Distance")
    var distance: Double = 0.toDouble()
    @SerializedName("TotalPoint")
    var totalPoint: Int = 0
    @SerializedName("Latitude")
    var latitude: Double = 0.toDouble()
    @SerializedName("Longitude")
    var longitude: Double = 0.toDouble()
    @SerializedName("WebProfileUrl")
    var webProfileUrl: String? = null
    @SerializedName("About")
    var about: String? = null
    @SerializedName("AvatarVersion")
    var avatarVersion = 0
    @SerializedName("Address")
    var address: String? = null
    @SerializedName("Color")
    var color: String? = null
    @SerializedName("Type")
    var type: Int = 0
    @SerializedName("PhoneNumber")
    var phoneNumber = ""
    @SerializedName("Gems")
    var gems: Long = 0
    @SerializedName("Stars")
    var stars: Long = 0
    @SerializedName("Loyalty")
    var loyalty: Long = 0
    @SerializedName("RefId")
    var refId: Int = 0
    @SerializedName("GoogleId")
    var googleId: String? = ""
    @SerializedName("InstagramId")
    var instagramId: String? = ""
    @SerializedName("FbId")
    var fbId: String? = ""


    constructor()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(userId)
        writeString(roleId)
        writeString(referralId)
        writeString(userName)
        writeString(email)
        writeInt(emailVerified)
        writeString(emailToken)
        writeString(userImage)
        writeString(gender)
        writeString(doB)
        writeString(nationality)
        writeInt(notification)
        writeInt(searchable)
        writeInt(notificationSound)
        writeInt(status)
        writeString(created)
        writeLong(followerCount)
        writeLong(followingCount)
        writeInt(isFollow)
        writeInt(unreadMessageCount)
        writeInt(unreadNotificationCount)
        writeInt(loginLogoutStatus)
        writeDouble(distance)
        writeInt(totalPoint)
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(webProfileUrl)
        writeString(about)
        writeInt(avatarVersion)
        writeString(address)
        writeString(color)
        writeLong(gems)
        writeLong(stars)
        writeLong(loyalty)
        writeString(googleId)
        writeString(instagramId)
        writeString(fbId)
    }

    protected constructor(p: Parcel) {
        userId = p.readInt()
        roleId = p.readString()
        referralId = p.readString()
        userName = p.readString()
        email = p.readString()
        emailVerified = p.readInt()
        emailToken = p.readString()
        userImage = p.readString()
        gender = p.readString()
        doB = p.readString()
        nationality = p.readString()
        notification = p.readInt()
        searchable = p.readInt()
        notificationSound = p.readInt()
        status = p.readInt()
        created = p.readString()
        followerCount = p.readLong()
        followingCount = p.readLong()
        isFollow = p.readInt()
        unreadMessageCount = p.readInt()
        unreadNotificationCount = p.readInt()
        loginLogoutStatus = p.readInt()
        distance = p.readDouble()
        totalPoint = p.readInt()
        latitude = p.readDouble()
        longitude = p.readDouble()
        webProfileUrl = p.readString()
        about = p.readString()
        avatarVersion = p.readInt()
        address = p.readString()
        color = p.readString()
        gems = p.readLong()
        stars = p.readLong()
        loyalty = p.readLong()
        googleId = p.readString()
        instagramId = p.readString()
        fbId = p.readString()
    }

    fun isLoginWithFacebook(): Boolean {
        if (fbId.isNullOrEmpty()) return false
        return true
    }

    fun isLoginWithInstagram(): Boolean {
        if (instagramId.isNullOrEmpty()) return false
        return true
    }

    fun isLoginWithGmail(): Boolean {
        if (googleId.isNullOrEmpty()) return false
        return true
    }

    companion object {
        @JvmStatic
        fun getUserImageByUserNameAndTime(username: String): String {
            return getUserImageByUserName(username) + "?t=" + System.currentTimeMillis()
        }

        @JvmStatic
        fun getUserImageThumbByUserNameAndTime(username: String): String {
            return getUserImageThumbByUserName(username) + "?t=" + System.currentTimeMillis()
        }

        @JvmStatic
        fun getUserImageByUserName(userName: String?): String {
            return "${BuildConfig.AWS_S3_SERVER_LINK}/profile_image/$userName.jpg"
        }

        @JvmStatic
        fun getUserImageThumbByUserName(userName: String): String {
            return "${BuildConfig.AWS_S3_SERVER_LINK}/profile_image_thum/$userName.jpg"
        }

        @JvmField
        val CREATOR = parcelableCreator(::UserModel)
    }
}