package com.gcox.fansmeet.features.profile.userprofile

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityListUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityProfileUseCase
import com.gcox.fansmeet.domain.interactors.challenhes.CanSubmitChallengeUseCase
import com.gcox.fansmeet.domain.interactors.post.DeleteChallengeUseCase
import com.gcox.fansmeet.domain.interactors.useraction.*
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityResponse
import com.gcox.fansmeet.models.FollowUserModel
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel


/**
 * Created by Ngoc on 5/18/18.
 */
class UserProfileListViewModel constructor(
    private val getCelebrityUseCase: GetCelebrityListUseCase,
    private val getCelebrityProfileUseCase: GetCelebrityProfileUseCase,
    private val likeUseCase: LikeUseCase,
    private val unlikeUseCase: UnlikeUseCase,
    private val canSubmitChallengeUseCase: CanSubmitChallengeUseCase,
    private val deleteChallengeUseCase: DeleteChallengeUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val reportPostUserUseCase: ReportPostUserUseCase
) : BaseViewModel() {

    private var celebrityResponse = MutableLiveData<CelebrityResponse>()
    val getCelebrity = celebrityResponse

    private var celebrityProfileResponse = MutableLiveData<CelebrityProfileModel>()
    val getCelebrityProfile = celebrityProfileResponse

    private var likeResponse = MutableLiveData<LikePostResponse>()
    val getLikeResponse = likeResponse

    private var unlikeResponse = MutableLiveData<LikePostResponse>()
    val getUnlikeResponse = unlikeResponse

    private var canSubmitChallengeResponse = MutableLiveData<Boolean>()
    val canSubmitChallenge = canSubmitChallengeResponse

    private var deleteChallengeResponse = MutableLiveData<Boolean>()
    val deleteChallenge = deleteChallengeResponse

    private var followUseResponse = MutableLiveData<FollowUserModel>()
    val followUse = followUseResponse

    private var unFollowUseResponse = MutableLiveData<FollowUserModel>()
    val unFollowUse = unFollowUseResponse

    private var blockUserResponse = MutableLiveData<Boolean>()
    val blockUser = blockUserResponse

    private var reportPostResponse = MutableLiveData<ReportResponse>()
    val reportPost = reportPostResponse

    fun getCelebrityList(userId: Int, userName: String, nextId: Int, isRefresh: Boolean) {
        runUseCase(getCelebrityUseCase, CelebrityRequestModel(userId, userName, nextId, Constants.PAGE_LIMITED)) {
            it.isRefresh = isRefresh
            celebrityResponse.value = it
        }
    }

    fun getCelebrityProfile(userId: Int, userName: String) {
        runUseCase(getCelebrityProfileUseCase, GetCelebrityProfileUseCase.Params.load(userId, userName)) {
            celebrityProfileResponse.value = it
        }
    }

    fun like(postId: Int, position: Int,typeLike:Int) {
        runUseCase(likeUseCase, LikeUseCase.Params.load(postId, typeLike)) {
            likeResponse.value = LikePostResponse(it, postId, position)
        }
    }

    fun unlike(postId: Int, position: Int,typeLike:Int) {
        runUseCase(unlikeUseCase, UnlikeUseCase.Params.load(postId, typeLike)) {
            unlikeResponse.value = LikePostResponse(it, postId, position)
        }
    }

    fun checkCanSubmitChallenge(challengeId: Int) {
        runUseCase(canSubmitChallengeUseCase, challengeId) {
            canSubmitChallengeResponse.value = it
        }
    }

    fun deleteChallenge(challengeId: Int, type: Int) {
        runUseCase(deleteChallengeUseCase, DeleteChallengeUseCase.Params.load(challengeId, type)) {
            deleteChallengeResponse.value = it
        }
    }

    fun followUser(userId: Int) {
        runUseCase(followUseCase, userId) {
            followUseResponse.value = it
        }
    }

    fun unFollowUser(userId: Int) {
        runUseCase(unFollowUseCase, userId) {
            unFollowUseResponse.value = it
        }
    }

    fun blockUser(userId: Int) {
        runUseCase(blockUserUseCase, userId) {
            blockUserResponse.value = it
        }
    }

    fun reportPost(challengeId: Int, reason: String, type: Int, position: Int) {
        runUseCase(reportPostUserUseCase, ReportPostUserUseCase.Params.load(challengeId, reason, type)) {
            reportPostResponse.value = ReportResponse(challengeId, position, type)
        }
    }
}
