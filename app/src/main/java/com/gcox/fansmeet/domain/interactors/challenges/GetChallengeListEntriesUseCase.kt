package com.gcox.fansmeet.domain.interactors.challenges

import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.ChallengeRepository
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetChallengeListEntriesUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: ChallengeRepository
) : UseCase<BaseDataPagingResponseModel<DisplayableItem>, GetChallengeListEntriesUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: Params): Observable<BaseDataPagingResponseModel<DisplayableItem>> {
        return dataRepository.getChallengeListEntries(params.nextId, params.pageLimited)
    }

    class Params(var nextId: Int, var pageLimited: Int) {
        companion object {
            @JvmStatic
            fun loadPage(nextId: Int, pageLimited: Int): Params {
                return Params(nextId, pageLimited)
            }
        }
    }

}