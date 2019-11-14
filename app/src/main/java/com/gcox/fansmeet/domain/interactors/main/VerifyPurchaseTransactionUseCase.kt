package com.gcox.fansmeet.domain.interactors.main

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class VerifyPurchaseTransactionUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val mainRepository: MainRepository)
    : UseCase<VerifyIAPResponeModel, VerifyIAPRequestModel>(uiThread, executorThread){

    override fun buildObservable(params: VerifyIAPRequestModel): Observable<VerifyIAPResponeModel> {
        return mainRepository.verifyPurchasedWithServer(params)
    }

}