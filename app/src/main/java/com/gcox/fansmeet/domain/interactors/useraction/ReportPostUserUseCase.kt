package com.gcox.fansmeet.domain.interactors.useraction

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.GiftStoreRepository
import com.gcox.fansmeet.domain.repository.UserActionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class ReportPostUserUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: UserActionRepository
) : UseCase<Boolean, ReportPostUserUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(param: ReportPostUserUseCase.Params): Observable<Boolean> {
        return dataRepository.reportPost(param.challengeId, param.reason, param.type)
    }

    class Params(var challengeId: Int, var reason: String, var type: Int) {
        companion object {
            @JvmStatic
            fun load(challengeId: Int, reason: String, type: Int): Params {
                return Params(challengeId, reason, type)
            }
        }
    }

}