package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.repository.datasource.MainDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudMainDataSource
constructor(private val service: AppsterWebserviceAPI) : MainDataSource {
    override fun verifyPurchasedWithServer(request: VerifyIAPRequestModel): Observable<BaseResponse<VerifyIAPResponeModel>> {
        return service.verifyIAPPurchased(AppsterUtility.getAuth(), request)
    }

    override fun transactionChecking(transactionId: String): Observable<BaseResponse<Boolean>> {
        return service.iAPPurchasedIsfinished(AppsterUtility.getAuth(), transactionId)
    }

}