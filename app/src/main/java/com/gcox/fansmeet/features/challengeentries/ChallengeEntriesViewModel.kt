package com.gcox.fansmeet.features.challengeentries

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.challenges.DeleteSubmissionUseCase
import com.gcox.fansmeet.domain.interactors.challenhes.ViewContestantEntriesUseCase
import com.gcox.fansmeet.domain.interactors.comments.GetCommentsListUseCase
import com.gcox.fansmeet.domain.interactors.useraction.*
import com.gcox.fansmeet.features.profile.userprofile.LikeType
import com.gcox.fansmeet.features.profile.userprofile.ReportResponse
import com.gcox.fansmeet.models.FollowUserModel
import com.gcox.fansmeet.models.ItemClassComments
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class ChallengeEntriesViewModel constructor(
    private val ViewContestantEntriesUseCase: ViewContestantEntriesUseCase,
    private val likeUseCase: LikeUseCase,
    private val unlikeUseCase: UnlikeUseCase,
    private val getCommentsListUseCase: GetCommentsListUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase,
    private val reportEntriesUserUseCase: ReportEntriesUserUseCase,
    private val deteleteSubmissionUseCase: DeleteSubmissionUseCase
    ) : BaseViewModel() {

    private var contestantEntries = MutableLiveData<EntriesModel>()
    val viewContestantEntries = contestantEntries

    private var likeResponse = MutableLiveData<Int>()
    val getLikeResponse = likeResponse

    private var unlikeResponse = MutableLiveData<Int>()
    val getUnlikeResponse = unlikeResponse

    private var commentsListResponse = MutableLiveData<BaseDataPagingResponseModel<ItemClassComments>>()
    val getCommentsListResponse = commentsListResponse

    private var blockUserResponse = MutableLiveData<Boolean>()
    val blockUser = blockUserResponse

    private var followUseResponse = MutableLiveData<FollowUserModel>()
    val followUse = followUseResponse

    private var unFollowUseResponse = MutableLiveData<FollowUserModel>()
    val unFollowUse = unFollowUseResponse

    private var reportEntriesResponse = MutableLiveData<ReportResponse>()
    val reportEntries = reportEntriesResponse

    private var deleteSubmissionResponse = MutableLiveData<Boolean>()
    val deleteEntries = deleteSubmissionResponse

    fun viewContestantEntries(entriesId: Int) {
        runUseCase(ViewContestantEntriesUseCase, entriesId) {
            contestantEntries.value = it
        }
    }

    fun like(postId: Int) {
        runUseCase(likeUseCase,  LikeUseCase.Params.load(postId, LikeType.ENTRIES)) {
            likeResponse.value = it
        }
    }

    fun unlike(postId: Int) {
        runUseCase(unlikeUseCase, UnlikeUseCase.Params.load(postId, LikeType.ENTRIES)) {
            unlikeResponse.value = it
        }
    }

    fun getCommentsList(postId: Int, type: Int) {
        runUseCase(
            getCommentsListUseCase,
            GetCommentsListUseCase.Params.load(postId, Constants.WEB_SERVICE_START_PAGE, 2, type)
        ) {
            getCommentsListResponse.value = it
        }
    }

    fun blockUser(userId: Int) {
        runUseCase(blockUserUseCase, userId) {
            blockUserResponse.value = it
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

    fun reportEntries(challengeId: Int, reason: String, type: Int) {
        runUseCase(reportEntriesUserUseCase, ReportEntriesUserUseCase.Params.load(challengeId, reason, type)) {
            reportEntriesResponse.value = ReportResponse(challengeId, 0, type)
        }
    }

    fun deleteEntries(submissionId: Int) {
        runUseCase(deteleteSubmissionUseCase, submissionId) {
            deleteSubmissionResponse.value = it
        }
    }

}