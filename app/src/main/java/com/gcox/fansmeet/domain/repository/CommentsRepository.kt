package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.domain.models.DeleteCommentResponseModel
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface CommentsRepository {
    fun getCommentsList(postId: Int, nextId: Int, limit: Int, type: Int): Observable<BaseDataPagingResponseModel<ItemClassComments>>

    fun addComment(requestModel: AddCommentRequestModel, type: Int): Observable<AddCommentReponse>

    fun deleteComment(commentId: Int, postId: Int, type: Int):Observable<DeleteCommentResponseModel>
}