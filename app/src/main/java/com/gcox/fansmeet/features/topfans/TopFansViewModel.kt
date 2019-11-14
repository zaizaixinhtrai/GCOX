package com.gcox.fansmeet.features.topfans

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.topfans.TopFansUseCase
import com.gcox.fansmeet.domain.models.TopFansResponseModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel


/**
 * Created by Ngoc on 5/18/18.
 */
class TopFansViewModel constructor(
    private val topFansUseCase: TopFansUseCase
) : BaseViewModel() {

    private var topFansResponse = MutableLiveData<TopFansResponseModel>()
    val getTopFans = topFansResponse

    fun getTopFans(userId: Int, nextId: Int, limit: Int) {
        runUseCase(topFansUseCase, TopFansUseCase.Params.load(userId, nextId,limit)) {
            topFansResponse.value = it
        }
    }

}
