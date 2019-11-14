package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.data.entity.mapper.HomeEntityMapper
import com.gcox.fansmeet.data.entity.request.HomeCelebritiesRequestEntity
import com.gcox.fansmeet.data.repository.datasource.HomeDataSource
import com.gcox.fansmeet.domain.repository.HomeRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.home.*
import com.gcox.fansmeet.webservice.request.HomeCelebritiesRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by thanhbc on 5/18/18.
 */
class HomeDataRepository constructor(@Remote private val homeDataSource: HomeDataSource) :
    HomeRepository {

    private var homeEntityMapper: HomeEntityMapper = HomeEntityMapper()
    override fun getUsers(request: HomeCelebritiesRequestModel): Observable<HomeModel> {
        return homeDataSource.getUsers(HomeCelebritiesRequestEntity(request.nextId, request.limit,request.type)).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(homeEntityMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }

//            val response: BaseResponse<List<DisplayableItem>> = BaseResponse()
//            response.data = setupList()
//            response.code = 1
//            Observable.just(response)
        }
    }

    override fun getBanners(): Observable<BaseResponse<List<String>>> {
        return homeDataSource.getBanners().flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    fun setupList(): List<DisplayableItem> {
        var listUser = arrayListOf<DisplayableItem>()

        for (i in 1..20) {
//            listUser.add(HomeModel(""))
        }

        return listUser
    }
}