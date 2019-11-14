package com.gcox.fansmeet.domain.interactors.loyalty

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.LoyaltyRepository
import com.gcox.fansmeet.domain.repository.StarsRepository
import com.gcox.fansmeet.features.loyalty.LoyaltyResponse
import com.gcox.fansmeet.features.stars.StarResponse
import com.gcox.fansmeet.features.stars.StarsModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class LoyaltyUseCase constructor(uiThread: Scheduler, executorThread: Scheduler, private val dataRepository: LoyaltyRepository)
    : UseCase<LoyaltyResponse, LoyaltyUseCase.Params>(uiThread, executorThread){

    override fun buildObservable(params: LoyaltyUseCase.Params): Observable<LoyaltyResponse> {
        return dataRepository.getLoyaltyList(params.nextId,params.limit)
    }

    class Params(var nextId: Int, var limit: Int) {
        companion object {
            @JvmStatic
            fun load(nextId: Int, limit: Int): Params {
                return Params(nextId, limit)
            }
        }
    }

}