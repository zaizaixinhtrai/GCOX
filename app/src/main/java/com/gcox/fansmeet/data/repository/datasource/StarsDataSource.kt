package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface StarsDataSource {
    fun getStarsList(nextId: Int, limit: Int): Observable<BaseResponse<StarEntity>>
}