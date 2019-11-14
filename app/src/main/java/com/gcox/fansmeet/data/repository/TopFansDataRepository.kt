package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.TopFansMapper
import com.gcox.fansmeet.data.repository.datasource.TopFansDataSource
import com.gcox.fansmeet.domain.models.TopFansResponseModel
import com.gcox.fansmeet.domain.repository.TopFansRepository
import com.gcox.fansmeet.exception.GcoxException
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class TopFansDataRepository constructor(@Remote private val topFansDataSource: TopFansDataSource) :
    TopFansRepository {

    private val topFansMapper = TopFansMapper()

    override fun getTopFans(
        userId: Int,
        nextId: Int,
        limit: Int
    ): Observable<TopFansResponseModel> {
        return topFansDataSource.getTopFans(userId, nextId, limit).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(topFansMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

}