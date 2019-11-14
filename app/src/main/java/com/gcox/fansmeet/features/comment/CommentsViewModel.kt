package com.gcox.fansmeet.features.comment

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.comments.AddCommentUseCase
import com.gcox.fansmeet.domain.interactors.comments.DeleteCommentResponse
import com.gcox.fansmeet.domain.interactors.comments.DeleteCommentUseCase
import com.gcox.fansmeet.domain.interactors.comments.GetCommentsListUseCase
import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

/**
 * Created by Ngoc on 5/18/18.
 */
class CommentsViewModel constructor(
    private val getCommentsListUseCase: GetCommentsListUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : BaseViewModel() {

    private var commentsListResponse = MutableLiveData<BaseDataPagingResponseModel<ItemClassComments>>()
    val getCommentsListResponse = commentsListResponse

    private var addCommentResponse = MutableLiveData<AddCommentReponse>()
    val getCommentResponse = addCommentResponse

    private var deleteCommentResponse = MutableLiveData<DeleteCommentResponse>()
    val getDeleteCommentResponse = deleteCommentResponse

    fun getCommentsList(postId: Int, nextId: Int, limit: Int, type: Int) {
        runUseCase(
            getCommentsListUseCase, GetCommentsListUseCase.Params.load(postId, nextId, limit, type)
        ) {
            getCommentsListResponse.value = it
        }
    }

    fun addComment(requestModel: AddCommentRequestModel, type: Int) {
        runUseCase(
            addCommentUseCase, AddCommentUseCase.Params.load(requestModel, type)
        ) {
            addCommentResponse.value = it
        }
    }

    fun deleteComment(position: Int, commentId: Int, postId: Int, type: Int) {
        runUseCase(
            deleteCommentUseCase, DeleteCommentUseCase.Params.load(commentId, postId, type)
        ) {
            deleteCommentResponse.value = DeleteCommentResponse(position, commentId, it.commentCount)
        }
    }

}
