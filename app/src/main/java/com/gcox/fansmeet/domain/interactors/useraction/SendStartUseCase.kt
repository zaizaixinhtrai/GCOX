package com.gcox.fansmeet.domain.interactors.useraction

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.domain.repository.UserActionRepository
import com.gcox.fansmeet.features.profile.userprofile.gift.SendGiftModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class SendStartUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: GiftStoreRepository)
    : UseCase<SendGiftModel, SendStartUseCase.Params>(uiThread, executorThread){

    override fun buildObservable(params: Params): Observable<SendGiftModel> {
        return dataRepository.sendStart(params.giftId,params.receiverId)
    }

    class Params(var giftId: Int, var receiverId: Int) {
        companion object {
            @JvmStatic
            fun load(giftId: Int, receiverId: Int): Params {
                return Params(giftId, receiverId)
            }
        }
    }

}