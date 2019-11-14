package com.gcox.fansmeet.domain.interactors.reward

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class RewardListUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val rewardRepository: RewardRepository
) : UseCase<RewardListResponse, RewardListUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(param: Params): Observable<RewardListResponse> {
        return rewardRepository.getRewardList(param.nextId, param.limit)
    }

    class Params(var nextId: Int, var limit: Int) {
        companion object {
            @JvmStatic
            fun load(nextId: Int, limit: Int): Params {
                return Params(nextId, limit)
            }
        }
    }

}