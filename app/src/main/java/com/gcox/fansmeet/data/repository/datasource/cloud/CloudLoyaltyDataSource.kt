package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.LoyaltyEntity
import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.data.repository.datasource.LoyaltyDataSource
import com.gcox.fansmeet.data.repository.datasource.StarsDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudLoyaltyDataSource
constructor(private val service: AppsterWebserviceAPI) : LoyaltyDataSource {

    override fun getLoyaltyList(nextId: Int, limit: Int): Observable<BaseResponse<LoyaltyEntity>> {
        return service.getLoyaltyList(AppsterUtility.getAuth(),nextId,limit)
    }
}