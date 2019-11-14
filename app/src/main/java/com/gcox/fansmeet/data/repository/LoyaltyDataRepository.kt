package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.LoyaltyMapper
import com.gcox.fansmeet.data.repository.datasource.LoyaltyDataSource
import com.gcox.fansmeet.domain.repository.LoyaltyRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.loyalty.LoyaltyResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class LoyaltyDataRepository constructor(@Remote private val starsDataSource: LoyaltyDataSource) :
    LoyaltyRepository {
    override fun getLoyaltyList(nextId: Int, limit: Int): Observable<LoyaltyResponse> {
        return starsDataSource.getLoyaltyList(nextId,limit).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(loyaltyMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    private val loyaltyMapper by lazy { LoyaltyMapper() }
}