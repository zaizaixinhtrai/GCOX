package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.data.entity.CelebrityListEntity
import com.gcox.fansmeet.data.entity.ChallengeEntriesEntityResponse
import com.gcox.fansmeet.data.entity.ChallengeListEntriesEntityResponse
import com.gcox.fansmeet.data.entity.ContestantEntriesEntity
import com.gcox.fansmeet.features.challengeentries.EntriesModel
import com.gcox.fansmeet.features.challenges.ChallengeModel
import com.gcox.fansmeet.features.challenges.Entries
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesModel
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class ChallengeMapper {

    fun transform(entity: CelebrityListEntity?): CelebrityModel? {
        return entity?.let {
            return CelebrityModel(
                userId = it.userId,
                userName = it.userName,
                displayName = it.displayName,
                userImage = it.userImage,
                description = it.description,
                id = it.id,
                userBannerImage = it.userBannerImage,
                title = it.title,
                mediaType = it.mediaType,
                image = it.image,
                video = it.video,
                commentCount = it.commentCount,
                likeCount = it.likeCount,
                startedAt = it.startedAt,
                endedAt = it.endedAt,
                created = it.created,
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
                },
                hashTag = it.hashTag,
                prizeText = it.prizeText,
                entryCount = it.entryCount,
                isJoined = it.isJoined,
                isReachSubmissionLimit = it.isReachSubmissionLimit,
                isLike = it.isLike,
                maxSubmission = it.maxSubmission,
                selfieImage = it.selfieImage,
                webPostUrl = it.webPostUrl
            )
        }
    }

    fun transform(entity: ChallengeEntriesEntityResponse?): JoinChallengeEntriesResponse? {
        return entity?.let {
            return JoinChallengeEntriesResponse(
                isEnd = it.isEnd,
                nextId = it.nextId,
                result = it.result?.map {
                    JoinChallengeEntriesModel(
                        id = it.id,
                        userId = it.userId,
                        userName = it.userName,
                        displayName = it.displayName,
                        userImage = it.userImage,
                        userBannerImage = it.userBannerImage,
                        starCount = it.starCount,
                        image = it.image,
                        mediaType = it.mediaType
                    )
                }
            )
        }
    }

    fun transform(entity: ContestantEntriesEntity?): EntriesModel? {
        return entity?.let {
            return EntriesModel(
                displayName = it.displayName,
                userImage = it.userImage,
                userBannerImage = it.userBannerImage,
                isLike = it.isLike,
                id = it.id,
                challengeId = it.challengeId,
                userId = it.userId,
                caption = it.caption,
                image = it.image,
                video = it.video,
                starCount = it.starCount,
                commentCount = it.commentCount,
                likeCount = it.likeCount,
                tagUsers = it.tagUsers,
                created = it.created,
                isReport = it.isReported,
                mediaType = it.mediaType,
                isFollow = it.isFollowed,
                selfieImage = it.selfieImage,
                webPostUrl = it.webPostUrl,
                userName = it.userName
            )
        }
    }

    fun transform(entity: ChallengeListEntriesEntityResponse?): BaseDataPagingResponseModel<DisplayableItem>? {
        return entity?.let {

            val response = BaseDataPagingResponseModel<DisplayableItem>()
            response.isEnd = it.isEnd!!
            response.nextId = it.nextId!!
            response.result = it.result?.map {
                ChallengeModel(
                    it.id,
                    it.hashTag,
                    it.entryCount,
                    it.entries?.map {
                        Entries(
                            it.id,
                            it.userId,
                            it.userName,
                            it.displayName,
                            it.userImage,
                            it.userBannerImage,
                            it.starCount,
                            it.image,
                            it.mediaType
                        )
                    },
                    it.title,
                    it.mediaType
                )
            }

            return response
        }
    }
}
