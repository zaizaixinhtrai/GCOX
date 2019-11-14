package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.*
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface CommentsDataSource {
    fun getCommentsList(
        postId: Int,
        nextId: Int,
        limit: Int,
        type: Int
    ): Observable<BaseResponse<BaseDataPagingResponseModel<CommentsListEntity>>>

    fun addComment(requestModel: AddCommentRequestModel, type: Int): Observable<BaseResponse<AddCommentEntity>>

    fun deleteComment(commentId: Int, postId: Int, type: Int):Observable<BaseResponse<DeleteCommentEntity>>
}