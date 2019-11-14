package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.TopFansEntity
import com.gcox.fansmeet.data.repository.datasource.TopFansDataSource
import com.gcox.fansmeet.data.repository.datasource.UserActionDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.SendStartRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudTopFansDataSource
constructor(private val service: AppsterWebserviceAPI) : TopFansDataSource {


    override fun getTopFans(userId: Int, nextId: Int, limit: Int): Observable<BaseResponse<TopFansEntity>> {
        return service.getTopFans(AppsterUtility.getAuth(), userId, nextId, limit)
    }

}