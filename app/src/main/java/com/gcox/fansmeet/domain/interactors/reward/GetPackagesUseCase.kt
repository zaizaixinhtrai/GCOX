package com.gcox.fansmeet.domain.interactors.reward

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetPackagesUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val rewardRepository: RewardRepository
) : UseCase<CelebrityBoxesModel, Int>(uiThread, executorThread) {

    override fun buildObservable(ownerId: Int): Observable<CelebrityBoxesModel> {
        return rewardRepository.getPackages(ownerId)
    }
}