package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.FollowUserEntity
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.data.repository.datasource.UserActionDataSource
import com.gcox.fansmeet.features.profile.userprofile.LikeType
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.FollowUserRequestModel
import com.gcox.fansmeet.webservice.request.ReportRequestModel
import com.gcox.fansmeet.webservice.request.SendGiftRequest
import com.gcox.fansmeet.webservice.request.SendStartRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudUserActionDataSource
constructor(private val service: AppsterWebserviceAPI) :
    UserActionDataSource {

    override fun reportEntries(postId: Int, reason: String, type: Int): Observable<BaseResponse<Boolean>> {
        return if (type == Constants.REPORT_TYPE) {
            service.reportEntries(AppsterUtility.getAuth(), postId, ReportRequestModel(reason))
        } else {
            service.unReportEntries(AppsterUtility.getAuth(), postId)
        }
    }

    override fun reportPost(postId: Int, reason: String, type: Int): Observable<BaseResponse<Boolean>> {
        return if (type == Constants.REPORT_TYPE) {
            service.reportPost(AppsterUtility.getAuth(), postId, ReportRequestModel(reason))
        } else {
            service.unReportPost(AppsterUtility.getAuth(), postId)
        }
    }

    override fun blockUser(userId: Int): Observable<BaseResponse<Boolean>> {
        return service.blockUser(AppsterUtility.getAuth(), userId)
    }

    override fun unFollowUser(followUserId: Int): Observable<BaseResponse<FollowUserEntity>> {
        return service.unFollowUser(AppsterUtility.getAuth(), FollowUserRequestModel(followUserId))
    }

    override fun unlike(postId: Int, type: Int): Observable<BaseResponse<Int>> {
        return if (type == LikeType.CHALLENGES) {
            service.unlike(AppsterUtility.getAuth(), postId)
        } else {
            service.unlikeEntries(AppsterUtility.getAuth(), postId)
        }
    }

    override fun likePost(postId: Int, type: Int): Observable<BaseResponse<Int>> {
        return if (type == LikeType.CHALLENGES) {
            service.likePost(AppsterUtility.getAuth(), postId)
        } else {
            service.likeEntries(AppsterUtility.getAuth(), postId)
        }
    }

    override fun followUser(followUserId: Int): Observable<BaseResponse<FollowUserEntity>> {
        return service.followUser(AppsterUtility.getAuth(), FollowUserRequestModel(followUserId))
    }

    override fun sendStart(request: Int, start: Int): Observable<BaseResponse<SendGiftEntity>> {
        return service.sendGift(AppsterUtility.getAuth(), SendGiftRequest(start,1))
    }
}