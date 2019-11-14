package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.stars.StarResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface StarsRepository{
    fun getStartsList(nextId: Int, limit: Int) : Observable<StarResponse>

}