package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.AddCommentEntity
import com.gcox.fansmeet.data.entity.CommentsListEntity
import com.gcox.fansmeet.data.entity.DeleteCommentEntity
import com.gcox.fansmeet.domain.models.AddCommentReponse
import com.gcox.fansmeet.domain.models.DeleteCommentResponseModel
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class CommentsMapper {
    fun transform(entity: BaseDataPagingResponseModel<CommentsListEntity>?): BaseDataPagingResponseModel<ItemClassComments>? {
        return entity?.let {
            val response = BaseDataPagingResponseModel<ItemClassComments>()
            response.isEnd = it.isEnd
            response.nextId = it.nextId
            response.result = it.result?.map {
                ItemClassComments(
                    commentId = it.commentId!!,
                    displayName = it.displayName!!,
                    userImage = it.userImage!!,
                    gender = it.gender!!,
                    userName = it.userName,
                    userId = it.userId,
                    created = it.created,
                    message = it.message
                )
            }

            return response
        }
    }

    fun transform(entity: AddCommentEntity?): AddCommentReponse?{
        return entity?.let {
            AddCommentReponse(commentId = it.commentId,
                message = it.message,
                created = it.created,
                commentCounts = it.commentCount)
        }
    }

    fun transform(entity: DeleteCommentEntity?): DeleteCommentResponseModel?{
        return entity?.let {
            DeleteCommentResponseModel(it.commentCount)
        }
    }

}

