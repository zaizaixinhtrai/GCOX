package com.gcox.fansmeet.domain.interactors.home

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.HomeRepository
import com.gcox.fansmeet.features.home.HomeModel
import com.gcox.fansmeet.webservice.request.HomeCelebritiesRequestModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by thanhbc on 5/18/18.
 */
class GetUsersUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: HomeRepository)
    : UseCase<HomeModel, HomeCelebritiesRequestModel>(uiThread, executorThread){

    override fun buildObservable(params: HomeCelebritiesRequestModel): Observable<HomeModel> {
        return dataRepository.getUsers(params)
    }

}