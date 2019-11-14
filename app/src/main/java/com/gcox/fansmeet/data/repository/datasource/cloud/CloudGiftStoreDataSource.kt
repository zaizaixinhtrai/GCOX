package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.GiftStoreEntity
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.data.repository.datasource.GiftStoreDataSource
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.SendGiftRequest
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 3/27/18.
 */
class CloudGiftStoreDataSource(val service: AppsterWebserviceAPI, val authen: String = "") : GiftStoreDataSource {

    override fun sendStars(giftId: Int, receiverId: Int): Observable<BaseResponse<SendGiftEntity>> {
        return service.sendGift(authen, SendGiftRequest(giftId, receiverId))
    }

    override fun fetchGiftStore(): Observable<BaseResponse<GiftStoreEntity>> {
        return service.getGiftStore(authen)
    }
}