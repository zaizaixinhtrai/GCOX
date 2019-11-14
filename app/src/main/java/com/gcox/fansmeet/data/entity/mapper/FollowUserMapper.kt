package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.FollowUserEntity
import com.gcox.fansmeet.models.FollowUserModel

class FollowUserMapper {

    fun transform(entity: FollowUserEntity?): FollowUserModel? {
        return entity?.let {
           return  FollowUserModel(userFollowerCount = it.userFollowerCount,
                meFollowingCount = it.meFollowingCount)
        }
    }
}