package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.challengeentries.EntriesModel
import com.gcox.fansmeet.features.joinchallenge.JoinChallengeEntriesResponse
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface ChallengeRepository {
    fun getChallenge(request: Int): Observable<CelebrityModel>

    fun getChallengeEntries(challengeId: Int, nextId: Int, pageLimited: Int): Observable<JoinChallengeEntriesResponse>

    fun viewContestantChallengesEntries(request: Int) : Observable<EntriesModel>

    fun getChallengeListEntries(nextId: Int, pageLimited: Int): Observable<BaseDataPagingResponseModel<DisplayableItem>>

    fun canSubmitChallenge(challengeId: Int): Observable<Boolean>

    fun deleteSubmission(submissionId:Int): Observable<Boolean>
}