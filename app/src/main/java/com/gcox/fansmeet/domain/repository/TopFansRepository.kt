package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.domain.models.TopFansResponseModel
import com.gcox.fansmeet.features.topfans.TopFanModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by thanhbc on 5/18/18.
 */
interface TopFansRepository{
    fun getTopFans(userId: Int, nextId: Int, limit: Int) : Observable<TopFansResponseModel>

}