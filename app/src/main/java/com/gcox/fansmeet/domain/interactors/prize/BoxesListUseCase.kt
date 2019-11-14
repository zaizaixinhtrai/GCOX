package com.gcox.fansmeet.domain.interactors.prize

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.features.prizelist.models.BoxesModel
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.prizelist.models.PrizeResponse
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class BoxesListUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val rewardRepository: RewardRepository
) : UseCase<List<BoxesModel>, Int>(uiThread, executorThread) {

    override fun buildObservable(ownerId: Int): Observable<List<BoxesModel>> {
        return rewardRepository.getBoxesList(ownerId)
    }

}