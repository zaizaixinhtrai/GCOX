package com.gcox.fansmeet.domain.interactors.useraction

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.domain.repository.UserActionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class BlockUserUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: UserActionRepository)
    : UseCase<Boolean, Int>(uiThread, executorThread){

    override fun buildObservable(userId: Int): Observable<Boolean> {
        return dataRepository.blockUser(userId)
    }

}