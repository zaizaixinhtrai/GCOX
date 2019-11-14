package com.gcox.fansmeet.domain.interactors.prize

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.prizelist.models.PrizeResponse
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class PrizeListUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val rewardRepository: RewardRepository
) : UseCase<PrizeResponse, PrizeListUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(param: Params): Observable<PrizeResponse> {
        return rewardRepository.getPrizeList(param.type, param.nextId, param.limit)
    }

    class Params(var type: Int, var nextId: Int, var limit: Int) {
        companion object {
            @JvmStatic
            fun load(type: Int, nextId: Int, limit: Int): Params {
                return Params(type, nextId, limit)
            }
        }
    }

}