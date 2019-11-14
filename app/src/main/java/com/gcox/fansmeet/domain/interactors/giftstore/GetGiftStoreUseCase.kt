package com.domain.interactors.giftstore

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.models.GiftStoreModel
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by thanhbc on 3/27/18.
 */
class GetGiftStoreUseCase(val uiThread: Scheduler, val executorThread: Scheduler, private val giftStoreRepository: GiftStoreRepository
) : UseCase<GiftStoreModel, Unit>(uiThread, executorThread) {
    override fun buildObservable(params: Unit?): Observable<GiftStoreModel> {
        return giftStoreRepository.fetchGiftStore()
    }
}