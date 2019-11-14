package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.UsersEntity
import com.gcox.fansmeet.data.entity.request.HomeCelebritiesRequestEntity
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by thanhbc on 5/18/18.
 */
interface HomeDataSource{
    fun getUsers(request: HomeCelebritiesRequestEntity) : Observable<BaseResponse<UsersEntity>>

    fun getBanners(): Observable<BaseResponse<List<String>>>

}