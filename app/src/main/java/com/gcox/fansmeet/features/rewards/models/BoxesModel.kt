package com.gcox.fansmeet.features.rewards.models

import android.os.Parcelable
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.UpdateableItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Parcelize
data class CelebrityBoxesModel(
    @field:SerializedName("GemBalance")
    val gemBalance: Int? = 0,
    @field:SerializedName("LoyaltyBalance")
    val loyaltyBalance: Int? = 0,
    @field:SerializedName("Boxes")
    val boxes: List<BoxesModel>?
): Parcelable

@Parcelize
data class BoxesModel(
    @field:SerializedName("Id")
    val id: Int? = 0,
    @field:SerializedName("Name")
    val name: String? = "",
    @field:SerializedName("Type")
    val type: Int? = 0,
    @field:SerializedName("CanPlay")
    val canPlay: Boolean?,
    @field:SerializedName("RequiredGems")
    val requiredGems: Long? = 0,
    @field:SerializedName("RequiredLoyalty")
    val requiredLoyalty: Long? = 0

) : Parcelable, UpdateableItem {
    override fun isSameItem(item: DisplayableItem?): Boolean {
        return item is BoxesModel && item.id == this.id
    }

    override fun isSameContent(item: DisplayableItem?): Boolean {
        return item is BoxesModel && item.hashCode() == this.hashCode()
    }

    fun getPrizeCost(): String {
        return "$requiredLoyalty pts ${if (requiredGems != 0L) "+ $requiredGems :gem:" else ""} "
    }
}

