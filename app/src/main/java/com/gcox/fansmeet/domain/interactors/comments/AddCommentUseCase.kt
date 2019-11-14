package com.gcox.fansmeet.domain.interactors.comments

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.domain.repository.CommentsRepository
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class AddCommentUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: CommentsRepository
) : UseCase<AddCommentReponse, AddCommentUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: AddCommentUseCase.Params): Observable<AddCommentReponse> {
        return dataRepository.addComment(params.requestModel, params.type)
    }

    class Params(var requestModel: AddCommentRequestModel, var type: Int) {
        companion object {
            @JvmStatic
            fun load(requestModel: AddCommentRequestModel, type: Int): Params {
                return Params(requestModel, type)
            }
        }
    }

}