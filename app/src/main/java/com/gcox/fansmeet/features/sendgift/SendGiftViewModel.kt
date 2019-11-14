package com.gcox.fansmeet.features.sendgift

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.useraction.SendStartUseCase
import com.gcox.fansmeet.features.profile.userprofile.gift.SendGiftModel


/**
 * Created by Ngoc on 5/18/18.
 */
class SendGiftViewModel constructor(
    private val sendStartUseCase: SendStartUseCase
) : BaseViewModel() {

    private var sendStartResponse = MutableLiveData<SendGiftModel>()
    val getStart = sendStartResponse

    fun sendStart(entryId: Int, start: Int) {
        runUseCase(sendStartUseCase, SendStartUseCase.Params.load(entryId, start)) {
            sendStartResponse.value = it
        }
    }

}
