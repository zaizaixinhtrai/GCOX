package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.FollowMapper
import com.gcox.fansmeet.data.repository.datasource.FollowDataSource
import com.gcox.fansmeet.domain.repository.FollowRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class FollowDataRepository constructor(@Remote private val starsDataSource: FollowDataSource) :
    FollowRepository {
    override fun getFollowList(
        userId: Int,
        nextId: Int,
        limit: Int,
        type: Int
    ): Observable<BaseDataPagingResponseModel<FollowItemModel>>{
        return starsDataSource.getFollowList(userId,nextId,limit,type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(followMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    private val followMapper by lazy { FollowMapper() }
}