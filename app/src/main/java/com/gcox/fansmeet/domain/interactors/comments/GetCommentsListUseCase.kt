package com.gcox.fansmeet.domain.interactors.comments

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.CommentsRepository
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class GetCommentsListUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: CommentsRepository
) : UseCase<BaseDataPagingResponseModel<ItemClassComments>, GetCommentsListUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: GetCommentsListUseCase.Params): Observable<BaseDataPagingResponseModel<ItemClassComments>> {
        return dataRepository.getCommentsList(params.userId, params.nextId, params.limit,params.type)
    }

    class Params(var userId: Int, var nextId: Int, var limit: Int,var type:Int) {
        companion object {
            @JvmStatic
            fun load(userId: Int, nextId: Int, limit: Int,type:Int): Params {
                return Params(userId, nextId, limit,type)
            }
        }
    }

}