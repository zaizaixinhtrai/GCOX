package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface FollowRepository {
    fun getFollowList(userId: Int, nextId: Int, limit: Int,type:Int): Observable<BaseDataPagingResponseModel<FollowItemModel>>

}