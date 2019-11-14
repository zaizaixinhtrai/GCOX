package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.*
import com.gcox.fansmeet.webservice.request.ChallengeEntriesRequestModel
import com.gcox.fansmeet.webservice.request.ChallengeListEntriesRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface ChallengeDataSource{

    fun getChallenge(request: Int) : Observable<BaseResponse<CelebrityListEntity>>

    fun getChallengeEntries(request: ChallengeEntriesRequestModel) : Observable<BaseResponse<ChallengeEntriesEntityResponse>>

    fun viewContestantChallengesEntries(request: Int) : Observable<BaseResponse<ContestantEntriesEntity>>

    fun getChallengeListEntries(request: ChallengeListEntriesRequestModel) : Observable<BaseResponse<ChallengeListEntriesEntityResponse>>

    fun canSubmitChallenge(challengeId: Int): Observable<BaseResponse<Boolean>>

    fun deleteSubmission(submissionId:Int): Observable<BaseResponse<Boolean>>
}