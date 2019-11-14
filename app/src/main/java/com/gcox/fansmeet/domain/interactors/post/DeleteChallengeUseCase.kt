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

class DeleteChallengeUseCase constructor(
    uiThread: Scheduler, executorThread: Scheduler, private val mDataRepository: PostRepository)
    : UseCase<Boolean, DeleteChallengeUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: DeleteChallengeUseCase.Params): Observable<Boolean> {
        return mDataRepository.deleteChallenge(params.challengeId,params.type)
    }

    class Params(var challengeId: Int, var type: Int) {
        companion object {
            @JvmStatic
            fun load(challengeId: Int, type: Int): Params {
                return Params(challengeId, type)
            }
        }
    }

}
