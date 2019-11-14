package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.loyalty.LoyaltyResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface LoyaltyRepository{
    fun getLoyaltyList(nextId: Int, limit: Int) : Observable<LoyaltyResponse>

}