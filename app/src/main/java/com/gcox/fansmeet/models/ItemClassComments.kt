package com.gcox.fansmeet.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by User on 9/4/2015.
 */
@Parcelize
class ItemClassComments(
    var commentId: Int?,
    var message: String?,
    var displayName: String?,
    var userId: Int?,
    var userImage: String?,
    var created: String?,
    var gender: String?,
    var userName: String?,
    var userUpdated: String? = null
): Parcelable
