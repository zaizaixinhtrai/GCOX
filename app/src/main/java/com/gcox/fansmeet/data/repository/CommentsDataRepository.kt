package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.mapper.CommentsMapper
import com.gcox.fansmeet.data.repository.datasource.CommentsDataSource
import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.domain.models.DeleteCommentResponseModel
import com.gcox.fansmeet.domain.repository.CommentsRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CommentsDataRepository constructor(@Remote private val starsDataSource: CommentsDataSource) :
    CommentsRepository {
    override fun deleteComment(commentId: Int, postId: Int, type: Int): Observable<DeleteCommentResponseModel> {
        return starsDataSource.deleteComment(commentId,postId,type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(commentsMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun addComment(requestModel: AddCommentRequestModel, type: Int): Observable<AddCommentReponse> {
        return starsDataSource.addComment(requestModel,type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(commentsMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getCommentsList(
        postId: Int,
        nextId: Int,
        limit: Int,
        type: Int
    ): Observable<BaseDataPagingResponseModel<ItemClassComments>> {
        return starsDataSource.getCommentsList(postId,nextId,limit,type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(commentsMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }


    private val commentsMapper by lazy { CommentsMapper() }
}