package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface MainDataSource {
    fun transactionChecking(transactionId: String) : Observable<BaseResponse<Boolean>>
    fun verifyPurchasedWithServer(request: VerifyIAPRequestModel) : Observable<BaseResponse<VerifyIAPResponeModel>>
}