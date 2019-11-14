package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.FollowEntity
import com.gcox.fansmeet.data.entity.LoyaltyEntity
import com.gcox.fansmeet.data.repository.datasource.FollowDataSource
import com.gcox.fansmeet.features.follow.ActivityFollow
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudFollowDataSource
constructor(private val service: AppsterWebserviceAPI) : FollowDataSource {
    override fun getFollowList(
        userId: Int,
        nextId: Int,
        limit: Int,
        type: Int
    ): Observable<BaseResponse<BaseDataPagingResponseModel<FollowEntity>>> {
        return if (type == ActivityFollow.TypeList.FOLLOWING.ordinal) {
            service.getFollowingList(AppsterUtility.getAuth(), userId, nextId, limit)
        } else {
            service.getFollowersList(AppsterUtility.getAuth(), userId, nextId, limit)
        }
    }

}