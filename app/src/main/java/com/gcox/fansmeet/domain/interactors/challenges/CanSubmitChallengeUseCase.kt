package com.gcox.fansmeet.domain.interactors.challenhes

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class CanSubmitChallengeUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: ChallengeRepository)
    : UseCase<Boolean, Int>(uiThread, executorThread){

    override fun buildObservable(params: Int): Observable<Boolean> {
        return dataRepository.canSubmitChallenge(params)
    }

}