package com.gcox.fansmeet.features.stars

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.stars.StartUseCase
import com.gcox.fansmeet.domain.interactors.useraction.SendStartUseCase


/**
 * Created by Ngoc on 5/18/18.
 */
class StarsViewModel constructor(
    private val getStartUseCase: StartUseCase
) : BaseViewModel() {

    private var getStartsResponse = MutableLiveData<StarResponse>()
    val getStart = getStartsResponse

    fun getStarts(nextId: Int) {
        runUseCase(getStartUseCase, StartUseCase.Params.load(nextId, Constants.PAGE_LIMITED)) {
            getStartsResponse.value = it
        }
    }

}
