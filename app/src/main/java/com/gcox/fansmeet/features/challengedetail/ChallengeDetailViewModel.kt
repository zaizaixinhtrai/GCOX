package com.gcox.fansmeet.features.challengedetail

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.challenges.GetChallengeEntriesUseCase
import com.gcox.fansmeet.domain.interactors.challenges.GetChallengeUseCase
import com.gcox.fansmeet.domain.interactors.challenhes.CanSubmitChallengeUseCase
import com.gcox.fansmeet.domain.interactors.useraction.LikeUseCase
import com.gcox.fansmeet.domain.interactors.useraction.UnlikeUseCase
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.userprofile.LikeType

class ChallengeDetailViewModel constructor(
    private val getChallengeUseCase: GetChallengeUseCase,
    private val getChallengeEntriesUseCase: GetChallengeEntriesUseCase,
    private val likeUseCase: LikeUseCase,
    private val unlikeUseCase: UnlikeUseCase,
    private val canSubmitChallengeUseCase : CanSubmitChallengeUseCase
) : BaseViewModel() {

    private var challenge = MutableLiveData<CelebrityModel>()
    val getChallenge = challenge

    private var challengeEntries = MutableLiveData<JoinChallengeEntriesResponse>()
    val getChallengeEntries = challengeEntries

    private var likeResponse = MutableLiveData<Int>()
    val getLikeResponse = likeResponse

    private var unlikeResponse = MutableLiveData<Int>()
    val getUnlikeResponse = unlikeResponse

    private var canSubmitChallengeResponse = MutableLiveData<Boolean>()
    val canSubmitChallenge = canSubmitChallengeResponse

    fun getChallenge(challengeId: Int) {
        runUseCase(getChallengeUseCase, challengeId) {
            challenge.value = it
        }
    }

    fun getChallengeEntries(challengeId: Int, nextId: Int) {
        runUseCase(
            getChallengeEntriesUseCase,
            GetChallengeEntriesUseCase.Params.loadPage(challengeId, nextId, 21)
        ) {
            challengeEntries.value = it
        }
    }

    fun like(postId: Int) {
        runUseCase(likeUseCase, LikeUseCase.Params.load(postId, LikeType.CHALLENGES)) {
            likeResponse.value = it
        }
    }

    fun unlike(postId: Int) {
        runUseCase(unlikeUseCase, UnlikeUseCase.Params.load(postId, LikeType.CHALLENGES)) {
            unlikeResponse.value = it
        }
    }

    fun checkCanSubmitChallenge(challengeId: Int) {
        runUseCase(canSubmitChallengeUseCase, challengeId) {
            canSubmitChallengeResponse.value = it
        }
    }
}