package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.UsersEntity
import com.gcox.fansmeet.features.home.CelebritiesMode
import com.gcox.fansmeet.features.home.HomeModel

class HomeEntityMapper {

    fun transform(entity: UsersEntity?): HomeModel? {
        return entity?.let {
            return HomeModel(
                    isEnd = entity.isEnd,
                    nextId = entity.nextId,
                    result = entity.result.orEmpty().map {
                        CelebritiesMode().apply {
                            userId = it.userId
                            userName = it.userName
                            displayName = it.displayName
                            userImage = it.userImage
                            bannerImage = it.bannerImage
                            gender = it.gender
                            about = it.about
                            description = it.description
                            isFollow = it.isFollow
                            onlyShowBannerImage = it.onlyShowBannerImage
                            isClickable =it.isClickable
                        }
                    })

        }
    }
}