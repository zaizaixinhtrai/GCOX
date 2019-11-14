package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.AddCommentEntity
import com.gcox.fansmeet.data.entity.CommentsListEntity
import com.gcox.fansmeet.data.entity.DeleteCommentEntity
import com.gcox.fansmeet.data.repository.datasource.CommentsDataSource
import com.gcox.fansmeet.features.comment.CommentsType
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.AddCommentRequestModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudCommentDataSource
constructor(private val service: AppsterWebserviceAPI) : CommentsDataSource {

    override fun deleteComment(commentId: Int, postId: Int, type: Int): Observable<BaseResponse<DeleteCommentEntity>> {
        return if (type == CommentsType.CHALLENGES) {
            service.deleteChallengeComment(AppsterUtility.getAuth(), commentId, postId)
        } else {
            service.deleteSubmissionComment(AppsterUtility.getAuth(), commentId, postId)
        }
    }

    override fun addComment(
        requestModel: AddCommentRequestModel,
        type: Int
    ): Observable<BaseResponse<AddCommentEntity>> {
        return if (type == CommentsType.CHALLENGES) {
            service.addChallengeComment(AppsterUtility.getAuth(), requestModel)
        } else {
            service.addEntriesComment(AppsterUtility.getAuth(), requestModel)
        }
    }

    override fun getCommentsList(
        postId: Int,
        nextId: Int,
        limit: Int,
        type: Int
    ): Observable<BaseResponse<BaseDataPagingResponseModel<CommentsListEntity>>> {
        return if (type == CommentsType.CHALLENGES) {
            service.getChallengeCommentsList(AppsterUtility.getAuth(), postId, nextId, limit)
        } else {
            service.getEntriesCommentsList(AppsterUtility.getAuth(), postId, nextId, limit)
        }
    }

}