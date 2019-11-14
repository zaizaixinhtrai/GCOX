package com.gcox.fansmeet.domain.interactors.main

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

class IAPIsfinishedCheckingUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val mainRepository: MainRepository)
    : UseCase<Boolean, String>(uiThread, executorThread){

    override fun buildObservable(params: String): Observable<Boolean> {
        return mainRepository.transactionChecking(params)
    }

}