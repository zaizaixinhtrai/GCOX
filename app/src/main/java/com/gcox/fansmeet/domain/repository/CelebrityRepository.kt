package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityGridResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityResponse
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel
import io.reactivex.Observable

/**
 * Created by thanhbc on 5/18/18.
 */
interface CelebrityRepository{
    fun getCelebrityList(request: CelebrityRequestModel) : Observable<CelebrityResponse>

    fun getCelebrityProfile(request: Int, username: String) : Observable<CelebrityProfileModel>

    fun getCelebrityGrid(request: CelebrityRequestModel) : Observable<CelebrityGridResponse>

}