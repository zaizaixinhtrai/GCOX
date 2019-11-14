package com.gcox.fansmeet.domain.interactors.challenhes

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import com.gcox.fansmeet.features.challengeentries.EntriesModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class ViewContestantEntriesUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: ChallengeRepository)
    : UseCase<EntriesModel, Int>(uiThread, executorThread){

    override fun buildObservable(params: Int): Observable<EntriesModel> {
        return dataRepository.viewContestantChallengesEntries(params)
    }

}