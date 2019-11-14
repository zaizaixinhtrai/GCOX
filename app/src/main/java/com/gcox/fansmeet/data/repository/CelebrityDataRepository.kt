package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.CelebrityEntityMapper
import com.gcox.fansmeet.data.entity.request.CelebrityListRequestEntity
import com.gcox.fansmeet.data.repository.datasource.CelebrityDataSource
import com.gcox.fansmeet.domain.repository.CelebrityRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityGridResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityResponse
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CelebrityDataRepository constructor(@Remote private val homeDataSource: CelebrityDataSource) :
    CelebrityRepository {

    private val celebrityEntityMapper = CelebrityEntityMapper()

    override fun getCelebrityGrid(request: CelebrityRequestModel): Observable<CelebrityGridResponse> {
        return homeDataSource.getCelebrityGrid(CelebrityListRequestEntity(request.celebrityId,request.userName,request.nextId,request.limit)).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(celebrityEntityMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getCelebrityProfile(request: Int, username: String): Observable<CelebrityProfileModel> {
        return homeDataSource.getCelebrityProfile(request,username).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(celebrityEntityMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }



    override fun getCelebrityList(request: CelebrityRequestModel): Observable<CelebrityResponse> {
        return homeDataSource.getCelebrityList(CelebrityListRequestEntity(request.celebrityId,request.userName,request.nextId,request.limit)).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(celebrityEntityMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }


}