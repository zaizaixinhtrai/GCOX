package com.gcox.fansmeet.domain.interactors.useraction

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.domain.repository.UserActionRepository
import com.gcox.fansmeet.models.FollowUserModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class UnFollowUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: UserActionRepository)
    : UseCase<FollowUserModel, Int>(uiThread, executorThread){

    override fun buildObservable(followUserId: Int): Observable<FollowUserModel> {
        return dataRepository.unFollowUser(followUserId)
    }

}