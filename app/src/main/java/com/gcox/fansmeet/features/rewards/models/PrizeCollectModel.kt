package com.gcox.fansmeet.features.rewards.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PrizeCollectModel(
    var id: Int,
    var name:String,
    var title: String?,
    var description: String?,
    var image: String?
) : Parcelable