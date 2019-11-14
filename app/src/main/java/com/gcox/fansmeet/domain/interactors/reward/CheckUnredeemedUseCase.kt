package com.gcox.fansmeet.domain.interactors.reward

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class CheckUnredeemedUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val rewardRepository: RewardRepository
) : UseCase<List<PrizeCollectModel>, Unit>(uiThread, executorThread) {

    override fun buildObservable(param: Unit): Observable<List<PrizeCollectModel>> {
        return rewardRepository.checkUnredeemed()
    }
}