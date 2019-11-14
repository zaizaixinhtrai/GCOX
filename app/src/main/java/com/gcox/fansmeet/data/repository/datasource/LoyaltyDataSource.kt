package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.LoyaltyEntity
import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface LoyaltyDataSource {
    fun getLoyaltyList(nextId: Int, limit: Int): Observable<BaseResponse<LoyaltyEntity>>
}