package com.gcox.fansmeet.features.prizelist

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.main.IAPIsfinishedCheckingUseCase
import com.gcox.fansmeet.domain.interactors.main.VerifyPurchaseTransactionUseCase
import com.gcox.fansmeet.domain.interactors.prize.PrizeListUseCase
import com.gcox.fansmeet.features.prizelist.models.PrizeResponse
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel


/**
 * Created by Ngoc on 5/18/18.
 */
class PrizeListFragmentViewModel constructor(
    private val prizeListUseCase: PrizeListUseCase
) : BaseViewModel() {

    val getPrizeList = MutableLiveData<PrizeResponse>()

    fun getPrizeList(type: Int, nextId: Int, limit: Int) {
        runUseCase(prizeListUseCase, PrizeListUseCase.Params.load(type, nextId, limit)) {
            getPrizeList.value = it
        }
    }

}
