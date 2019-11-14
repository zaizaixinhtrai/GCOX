package com.gcox.fansmeet.domain.interactors.challenges

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesResponse
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class GetChallengeEntriesUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: ChallengeRepository
) : UseCase<JoinChallengeEntriesResponse, GetChallengeEntriesUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: Params): Observable<JoinChallengeEntriesResponse> {
        return dataRepository.getChallengeEntries(params.challengeId, params.nextId, params.pageLimited)
    }

    class Params(var challengeId: Int, var nextId: Int, var pageLimited: Int) {
        companion object {
            @JvmStatic
            fun loadPage(challengeId: Int, nextId: Int, pageLimited: Int): Params {
                return Params(challengeId, nextId, pageLimited)
            }
        }
    }

}