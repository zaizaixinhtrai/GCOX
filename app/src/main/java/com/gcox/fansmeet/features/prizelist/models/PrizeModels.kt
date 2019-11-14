package com.gcox.fansmeet.features.prizelist.models

import android.os.Parcelable
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.UpdateableItem
import kotlinx.android.parcel.Parcelize

data class PrizeResponse(
    val nextId: Int?,
    val isEnded: Boolean?,
    val listPrize: List<Prize>?
)

data class Prize(
    val prizeId: Int,
    val prizeImg: String?,
    val prizeDesc: String?,
    val title:String?,
    val type:Int?,
    val webUrl: String? = ""
) : UpdateableItem {
    override fun isSameItem(item: DisplayableItem?): Boolean {
        return item is Prize && item.prizeId == this.prizeId
    }

    override fun isSameContent(item: DisplayableItem?): Boolean {
        return item is Prize && item.hashCode() == this.hashCode()
    }
}

@Parcelize
data class PrizeType(
    val id: Int?,
    val ownerId: Int?,
    val name: String?,
    val type: Int?,
    val cost: PrizeCost,
    var canPlay: Boolean?
) :
    Parcelable, UpdateableItem {
    override fun isSameItem(item: DisplayableItem?): Boolean {
        return item is PrizeType && item.id == this.id
    }

    override fun isSameContent(item: DisplayableItem?): Boolean {
        return item is PrizeType && item.hashCode() == this.hashCode()
    }
}

@Parcelize
data class PrizeCost(val pts: Long, val gems: Long) : Parcelable {
    override fun toString(): String {
        return "$pts pts ${if (gems != 0L) "+ $gems :gem:" else ""} "
    }
}