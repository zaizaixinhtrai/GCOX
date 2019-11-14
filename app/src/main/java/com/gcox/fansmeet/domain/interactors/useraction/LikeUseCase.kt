package com.gcox.fansmeet.domain.interactors.useraction

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.domain.repository.UserActionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class LikeUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: UserActionRepository)
    : UseCase<Int, LikeUseCase.Params>(uiThread, executorThread){

    override fun buildObservable(param:LikeUseCase.Params): Observable<Int> {
        return dataRepository.likePost(param.postId,param.type)
    }

    class Params(var postId: Int, var type: Int) {
        companion object {
            @JvmStatic
            fun load(postId: Int, type: Int): Params {
                return Params(postId, type)
            }
        }
    }

}