package com.gcox.fansmeet.domain.interactors.comments

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.domain.models.DeleteCommentResponseModel
import com.gcox.fansmeet.domain.repository.CommentsRepository
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class DeleteCommentUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: CommentsRepository
) : UseCase<DeleteCommentResponseModel, DeleteCommentUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: DeleteCommentUseCase.Params): Observable<DeleteCommentResponseModel> {
        return dataRepository.deleteComment(params.commentId, params.postId,params.type)
    }

    class Params(var commentId: Int, var postId: Int, var type: Int) {
        companion object {
            @JvmStatic
            fun load(commentId: Int, postId: Int, type: Int): Params {
                return Params(commentId, postId, type)
            }
        }
    }

}