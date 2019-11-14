package com.gcox.fansmeet.domain.interactors.challenges

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

class DeleteSubmissionUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: ChallengeRepository)
    : UseCase<Boolean, Int>(uiThread, executorThread){

    override fun buildObservable(params: Int): Observable<Boolean> {
        return dataRepository.deleteSubmission(params)
    }

}