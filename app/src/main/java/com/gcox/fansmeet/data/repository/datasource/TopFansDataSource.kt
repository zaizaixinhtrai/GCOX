package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.TopFansEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface TopFansDataSource {

    fun getTopFans(userId: Int, nextId: Int, limit: Int): Observable<BaseResponse<TopFansEntity>>

}