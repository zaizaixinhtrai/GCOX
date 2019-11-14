package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.GiftStoreEntity
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by thanhbc on 3/27/18.
 */
interface GiftStoreDataSource{
    fun fetchGiftStore() : Observable<BaseResponse<GiftStoreEntity>>

    fun sendStars(giftId: Int, receiverId: Int): Observable<BaseResponse<SendGiftEntity>>
}