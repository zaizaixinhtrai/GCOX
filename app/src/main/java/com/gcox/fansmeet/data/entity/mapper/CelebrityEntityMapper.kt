package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.CelebrityEntity
import com.gcox.fansmeet.data.entity.CelebrityGridEntity
import com.gcox.fansmeet.data.entity.CelebrityProfileEntity
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.*
import com.gcox.fansmeet.models.ItemClassComments

class CelebrityEntityMapper {

    fun transform(entity: CelebrityEntity?): CelebrityResponse? {
        return entity?.let {
            return CelebrityResponse(
                isEnd = entity.isEnd,
                nextId = entity.nextId,
                result = entity.result.orEmpty().map {
                    CelebrityModel().apply {
                        userId = it.userId
                        userName = it.userName
                        displayName = it.displayName
                        userImage = it.userImage
                        description = it.description
                        id = it.id
                        userBannerImage = it.userBannerImage
                        title = it.title
                        mediaType = it.mediaType
                        image = it.image
                        video = it.video
                        commentCount = it.commentCount
                        likeCount = it.likeCount
                        startedAt = it.startedAt
                        endedAt = it.endedAt
                        created = it.created
                        comments = it.comments?.map {
                            ItemClassComments(
                                commentId = it.id,
                                userId = it.userId,
                                displayName = it.displayName,
                                userName = it.userName,
                                message = it.message,
                                gender = it.gender,
                                userImage = it.image,
                                created = it.created,
                                userUpdated = it.userUpdated
                            )
                        }
                        hashTag = it.hashTag
                        prizeText = it.prizeText
                        isJoined = it.isJoined
                        isReachSubmissionLimit = it.isReachSubmissionLimit
                        postType = it.postType
                        isLike = it.isLike
                        isReport = it.isReport
                        maxSubmission = it.maxSubmission
                        selfieImage = it.selfieImage
                        webPostUrl = it.webPostUrl
                    }
                })

        }
    }

    fun transform(entity: CelebrityGridEntity?): CelebrityGridResponse? {
        return entity?.let {
            return CelebrityGridResponse(
                isEnd = entity.isEnd,
                nextId = entity.nextId,
                result = entity.result?.map {
                    GridModel(
                        id = it.id,
                        mediaType = it.mediaType,
                        image = it.image,
                        postType = it.postType
                    )
                }

            )
        }
    }

    fun transform(entity: CelebrityProfileEntity?): CelebrityProfileModel? {
        return entity?.let {
            return CelebrityProfileModel(
                followersCount = entity.followersCount,
                challengeCount = entity.challengeCount,
                giftReceivedCount = entity.giftReceivedCount,
                userId = entity.userId,
                userName = entity.userName,
                displayName = entity.displayName,
                userImage = entity.userImage,
                bannerImage = entity.bannerImage,
                gender = entity.gender,
                about = entity.about,
                description = entity.description,
                isFollow = entity.isFollow,
                type = entity.type,
                followingCount = entity.followingCount
            )
        }
    }
}