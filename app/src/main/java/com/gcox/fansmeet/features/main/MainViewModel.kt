package com.gcox.fansmeet.features.main

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.main.IAPIsfinishedCheckingUseCase
import com.gcox.fansmeet.domain.interactors.main.VerifyPurchaseTransactionUseCase
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel


/**
 * Created by Ngoc on 5/18/18.
 */
class MainViewModel constructor(
    private val iAPIsfinishedCheckingUseCase: IAPIsfinishedCheckingUseCase,
    private val veryfiPurchaseTransactionUseCase: VerifyPurchaseTransactionUseCase
) : BaseViewModel() {

    private var iAPIsfinishedChecking = MutableLiveData<Boolean>()
    val getiAPIsfinishedCheking = iAPIsfinishedChecking

    private var verifyIAPPurchasedResponse = MutableLiveData<VerifyIAPResponeModel>()
    val verifyIAPPurchased = verifyIAPPurchasedResponse

    fun iAPIsfinishedChecking(transactionId: String) {
        runUseCase(iAPIsfinishedCheckingUseCase, transactionId) {
            iAPIsfinishedChecking.value = it
        }
    }

    fun verifyIAPPurchased(request: VerifyIAPRequestModel) {
        runUseCase(veryfiPurchaseTransactionUseCase, request) {
            verifyIAPPurchasedResponse.value = it
            AppsterApplication.mAppPreferences.removeOrderObjectToList(request)
        }
    }

}
