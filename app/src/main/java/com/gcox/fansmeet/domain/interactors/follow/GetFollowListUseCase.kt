package com.gcox.fansmeet.domain.interactors.follow

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.FollowRepository
import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class GetFollowListUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: FollowRepository
) : UseCase<BaseDataPagingResponseModel<FollowItemModel>, GetFollowListUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: GetFollowListUseCase.Params): Observable<BaseDataPagingResponseModel<FollowItemModel>> {
        return dataRepository.getFollowList(params.userId, params.nextId, params.limit, params.type)
    }

    class Params(var userId: Int, var nextId: Int, var limit: Int, var type: Int) {
        companion object {
            @JvmStatic
            fun load(userId: Int, nextId: Int, limit: Int, type: Int): Params {
                return Params(userId, nextId, limit, type)
            }
        }
    }

}