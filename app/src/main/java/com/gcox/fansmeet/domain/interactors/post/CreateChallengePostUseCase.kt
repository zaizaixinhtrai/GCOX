package com.gcox.fansmeet.domain.interactors.post

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.data.repository.PostDataRepository
import com.gcox.fansmeet.domain.interactors.topfans.TopFansUseCase
import com.gcox.fansmeet.domain.repository.PostRepository
import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import okhttp3.MultipartBody

/**
 * Created by ngoc on 10/10/2017.
 */

class CreateChallengePostUseCase constructor(
    uiThread: Scheduler, executorThread: Scheduler, private val mDataRepository: PostRepository)
    : UseCase<BaseResponse<PostDataModel>, CreateChallengePostUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: CreateChallengePostUseCase.Params): Observable<BaseResponse<PostDataModel>> {
        return mDataRepository.createChallenge(params.multipartBody,params.isEdit,params.challengeId)
    }

    class Params(var multipartBody: MultipartBody,var isEdit:Boolean,var challengeId:Int) {
        companion object {
            @JvmStatic
            fun load(multipartBody: MultipartBody,isEdit:Boolean,challengeId:Int): Params {
                return Params(multipartBody,isEdit,challengeId)
            }
        }
    }

}
