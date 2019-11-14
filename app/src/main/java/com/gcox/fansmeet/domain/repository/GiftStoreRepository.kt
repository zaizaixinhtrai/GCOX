package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.domain.models.GiftStoreModel
import com.gcox.fansmeet.features.profile.userprofile.gift.SendGiftModel
import io.reactivex.Observable

/**
 * Created by thanhbc on 3/27/18.
 */
interface GiftStoreRepository{
    fun fetchGiftStore(): Observable<GiftStoreModel>
    fun sendStart(giftId: Int,receiverId:Int) : Observable<SendGiftModel>
}
