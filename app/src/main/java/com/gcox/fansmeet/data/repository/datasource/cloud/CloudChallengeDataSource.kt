package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.CelebrityListEntity
import com.gcox.fansmeet.data.entity.ChallengeEntriesEntityResponse
import com.gcox.fansmeet.data.entity.ChallengeListEntriesEntityResponse
import com.gcox.fansmeet.data.entity.ContestantEntriesEntity
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.data.repository.datasource.ChallengeDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.request.ChallengeEntriesRequestModel
import com.gcox.fansmeet.webservice.request.ChallengeListEntriesRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudChallengeDataSource
constructor(private val service: AppsterWebserviceAPI) :
    ChallengeDataSource {

    override fun deleteSubmission(submissionId: Int): Observable<BaseResponse<Boolean>> {
        return service.deleteSubmission(AppsterUtility.getAuth(),submissionId)
    }

    override fun canSubmitChallenge(challengeId: Int): Observable<BaseResponse<Boolean>> {
       return service.canSubmitChallenge(AppsterUtility.getAuth(),challengeId)
    }


    override fun getChallengeListEntries(request: ChallengeListEntriesRequestModel): Observable<BaseResponse<ChallengeListEntriesEntityResponse>> {
        return service.getChallengeListEntries(AppsterUtility.getAuth(), request.nextId, request.pageLimited)
    }


    override fun viewContestantChallengesEntries(request: Int): Observable<BaseResponse<ContestantEntriesEntity>> {
        return service.viewContestantChallengesEntries(AppsterUtility.getAuth(), request)
    }


    override fun getChallenge(request: Int): Observable<BaseResponse<CelebrityListEntity>> {
        return service.getChallenge(AppsterUtility.getAuth(), request)
    }

    override fun getChallengeEntries(request: ChallengeEntriesRequestModel): Observable<BaseResponse<ChallengeEntriesEntityResponse>> {
        return service.getChallengeEntries(AppsterUtility.getAuth(), request.challengeId, request.nextId, request.pageLimited)
    }
}