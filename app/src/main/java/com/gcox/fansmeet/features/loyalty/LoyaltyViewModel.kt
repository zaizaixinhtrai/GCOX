package com.gcox.fansmeet.features.loyalty

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.loyalty.LoyaltyUseCase
import com.gcox.fansmeet.domain.interactors.stars.StartUseCase
import com.gcox.fansmeet.domain.interactors.useraction.SendStartUseCase


/**
 * Created by Ngoc on 5/18/18.
 */
class LoyaltyViewModel constructor(
    private val getStartUseCase: LoyaltyUseCase
) : BaseViewModel() {

    private var getLoyaltyResponse = MutableLiveData<LoyaltyResponse>()
    val getLoyalty = getLoyaltyResponse

    fun getLoyalty(nextId: Int, limit: Int) {
        runUseCase(getStartUseCase, LoyaltyUseCase.Params.load(nextId,limit)) {
            getLoyaltyResponse.value = it
        }
    }

}
