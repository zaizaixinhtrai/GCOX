package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.CelebrityEntity
import com.gcox.fansmeet.data.entity.CelebrityGridEntity
import com.gcox.fansmeet.data.entity.CelebrityListEntity
import com.gcox.fansmeet.data.entity.CelebrityProfileEntity
import com.gcox.fansmeet.data.entity.request.CelebrityListRequestEntity
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.data.repository.datasource.CelebrityDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudCelebrityDataSource
constructor(private val service: AppsterWebserviceAPI) :
    CelebrityDataSource {

    override fun getCelebrityGrid(request: CelebrityListRequestEntity): Observable<BaseResponse<CelebrityGridEntity>> {
        return service.celebrityGrid(AppsterUtility.getAuth(), request.celebrityId, request.nextId, request.limit)
    }

    override fun getCelebrityProfile(userId: Int, username: String): Observable<BaseResponse<CelebrityProfileEntity>> {
        return if (username.isEmpty()) {
            service.celebrityProfile(AppsterUtility.getAuth(), userId)
        } else {
            service.getUserProfile(AppsterUtility.getAuth(), username)
        }
    }

    override fun getCelebrityList(request: CelebrityListRequestEntity): Observable<BaseResponse<CelebrityEntity>> {
        return service.homeCelebrityList(AppsterUtility.getAuth(), request.celebrityId, request.nextId, request.limit)
    }
}