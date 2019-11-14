package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.repository.datasource.MainDataSource
import com.gcox.fansmeet.domain.repository.MainRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class MainDataRepository constructor(@Remote private val mainDataSource: MainDataSource) :
    MainRepository {

    override fun verifyPurchasedWithServer(request: VerifyIAPRequestModel): Observable<VerifyIAPResponeModel> {
        return mainDataSource.verifyPurchasedWithServer(request).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun transactionChecking(transactionId: String): Observable<Boolean> {
        return mainDataSource.transactionChecking(transactionId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }
}