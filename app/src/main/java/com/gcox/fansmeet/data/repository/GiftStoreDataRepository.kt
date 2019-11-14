package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.GiftStoreEntityMapper
import com.gcox.fansmeet.data.repository.datasource.GiftStoreDataSource
import com.gcox.fansmeet.domain.models.GiftStoreModel
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.profile.userprofile.gift.SendGiftModel
import io.reactivex.Observable

/**
 * Created by thanhbc on 3/27/18.
 */
class GiftStoreDataRepository(val giftStoreDataSource: GiftStoreDataSource) : GiftStoreRepository {

    val mapper: GiftStoreEntityMapper by lazy { GiftStoreEntityMapper() }
    override fun fetchGiftStore(): Observable<GiftStoreModel> {
//        return giftStoreDataSource.fetchGiftStore()
//                .map(mapper::transform)
        return giftStoreDataSource.fetchGiftStore().flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(mapper.transform(it))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun sendStart(giftId: Int, receiverId: Int): Observable<SendGiftModel> {
        return giftStoreDataSource.sendStars(giftId, receiverId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(mapper.transform(it))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }
}