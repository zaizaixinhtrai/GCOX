package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.FollowEntity
import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class FollowMapper {
    fun transform(entity: BaseDataPagingResponseModel<FollowEntity>?): BaseDataPagingResponseModel<FollowItemModel>? {
        return entity?.let {

            val response = BaseDataPagingResponseModel<FollowItemModel>()
            response.isEnd = it.isEnd
            response.nextId = it.nextId
            response.result = it.result?.map {
                FollowItemModel(
                    userId = it.userId,
                    displayName = it.displayName,
                    userImage = it.userImage,
                    gender = it.gender,
                    isFollow = it.isFollow,
                    userName = it.userName,
                    about = it.about,
                    description = it.description,
                    bannerImage = it.bannerImage,
                    type = it.type
                )
            }

            return response
        }
    }

}

