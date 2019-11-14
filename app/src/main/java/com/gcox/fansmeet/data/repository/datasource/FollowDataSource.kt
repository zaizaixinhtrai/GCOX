package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.FollowEntity
import com.gcox.fansmeet.data.entity.LoyaltyEntity
import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface FollowDataSource {
    fun getFollowList(userId:Int, nextId: Int, limit: Int, type:Int): Observable<BaseResponse<BaseDataPagingResponseModel<FollowEntity>>>
}