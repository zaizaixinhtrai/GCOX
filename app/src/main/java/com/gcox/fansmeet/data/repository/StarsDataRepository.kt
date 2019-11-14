package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.StarsMapper
import com.gcox.fansmeet.data.repository.datasource.StarsDataSource
import com.gcox.fansmeet.domain.repository.StarsRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.stars.StarResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class StarsDataRepository constructor(@Remote private val starsDataSource: StarsDataSource) :
    StarsRepository {
    override fun getStartsList(nextId: Int, limit: Int): Observable<StarResponse> {
        return starsDataSource.getStarsList(nextId,limit).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(starsMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    private val starsMapper by lazy { StarsMapper() }
}