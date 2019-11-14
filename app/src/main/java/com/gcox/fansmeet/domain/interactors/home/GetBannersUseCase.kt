package com.gcox.fansmeet.domain.interactors.home

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.HomeRepository
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetBannersUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: HomeRepository)
    : UseCase<BaseResponse<List<String>>, Unit>(uiThread, executorThread){

    override fun buildObservable(params: Unit?): Observable<BaseResponse<List<String>>> {
        return dataRepository.getBanners()
    }

}