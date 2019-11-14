package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.data.repository.datasource.StarsDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudStarsDataSource
constructor(private val service: AppsterWebserviceAPI) : StarsDataSource {

    override fun getStarsList(nextId: Int, limit: Int): Observable<BaseResponse<StarEntity>> {
        return service.getStarsList(AppsterUtility.getAuth(),nextId,limit)
    }
}