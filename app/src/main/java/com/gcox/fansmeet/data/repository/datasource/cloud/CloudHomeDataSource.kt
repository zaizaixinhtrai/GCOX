package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.UsersEntity
import com.gcox.fansmeet.data.repository.datasource.HomeDataSource
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.data.entity.request.HomeCelebritiesRequestEntity
import com.gcox.fansmeet.features.home.HomeChildType
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudHomeDataSource
constructor(private val service: AppsterWebserviceAPI) :
    HomeDataSource {


    override fun getBanners(): Observable<BaseResponse<List<String>>> {
        return service.homeBanners(AppsterUtility.getAuth())
    }

    override fun getUsers(request: HomeCelebritiesRequestEntity): Observable<BaseResponse<UsersEntity>> {
        return if (request.type == HomeChildType.MERCHANTS) {
            service.getMerchants(AppsterUtility.getAuth(), request.nextId, request.limit)
        } else if (request.type == HomeChildType.EVENTS) {
            service.homeEvents(AppsterUtility.getAuth(), request.nextId, request.limit)
        } else if (request.type == HomeChildType.INFLUENCERS) {
            service.homeInfluencers(AppsterUtility.getAuth(), request.nextId, request.limit)
        } else {
            service.homeCelebrities(AppsterUtility.getAuth(), request.nextId, request.limit)
        }
    }

}