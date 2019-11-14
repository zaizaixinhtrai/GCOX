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
 * Created by linh on 10/10/2017.
 */

class CreatePostUseCase constructor(
    uiThread: Scheduler, executorThread: Scheduler, private val mDataRepository: PostRepository
) : UseCase<BaseResponse<PostDataModel>, CreatePostUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: CreatePostUseCase.Params): Observable<BaseResponse<PostDataModel>> {
        return mDataRepository.createPost(params.isEditing,params.isSubmissionChallenge, params.challengeId, params.multipartBody)
    }

    class Params(
        var isEditing: Boolean,
        var isSubmissionChallenge: Boolean,
        var challengeId: Int,
        var multipartBody: MultipartBody
    ) {
        companion object {
            @JvmStatic
            fun load(
                isEditing: Boolean,
                isSubmissionChallenge: Boolean,
                challengeId: Int,
                multipartBody: MultipartBody
            ): Params {
                return Params(isEditing, isSubmissionChallenge, challengeId, multipartBody)
            }
        }
    }

}
