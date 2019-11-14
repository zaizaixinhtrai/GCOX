package com.gcox.fansmeet.domain.interactors.topfans

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.models.TopFansResponseModel
import com.gcox.fansmeet.domain.repository.TopFansRepository
import com.gcox.fansmeet.features.topfans.TopFanModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class TopFansUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: TopFansRepository
) : UseCase<TopFansResponseModel, TopFansUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: Params): Observable<TopFansResponseModel> {
        return dataRepository.getTopFans(params.userId, params.nextId, params.limit)
    }

    class Params(var userId: Int, var nextId: Int, var limit: Int) {
        companion object {
            @JvmStatic
            fun load(userId: Int, nextId: Int, limit: Int): Params {
                return Params(userId, nextId, limit)
            }
        }
    }

}