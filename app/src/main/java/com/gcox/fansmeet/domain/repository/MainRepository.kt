package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface MainRepository{
    fun transactionChecking(transactionId: String) : Observable<Boolean>

    fun verifyPurchasedWithServer(request: VerifyIAPRequestModel) : Observable<VerifyIAPResponeModel>
}