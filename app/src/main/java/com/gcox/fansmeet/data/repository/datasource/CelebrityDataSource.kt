package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.*
import com.gcox.fansmeet.data.entity.request.CelebrityListRequestEntity
import com.gcox.fansmeet.data.entity.request.HomeCelebritiesRequestEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface CelebrityDataSource {
    fun getCelebrityList(request: CelebrityListRequestEntity): Observable<BaseResponse<CelebrityEntity>>

    fun getCelebrityProfile(request: Int, username: String): Observable<BaseResponse<CelebrityProfileEntity>>

    fun getCelebrityGrid(request: CelebrityListRequestEntity): Observable<BaseResponse<CelebrityGridEntity>>
}