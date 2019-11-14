package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.home.HomeModel
import com.gcox.fansmeet.webservice.request.HomeCelebritiesRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by thanhbc on 5/18/18.
 */
interface HomeRepository{
    fun getUsers(request: HomeCelebritiesRequestModel) : Observable<HomeModel>
    fun getBanners() : Observable<BaseResponse<List<String>>>

}