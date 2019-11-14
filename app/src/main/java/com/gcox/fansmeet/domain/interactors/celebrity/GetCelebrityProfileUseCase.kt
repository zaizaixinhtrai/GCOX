package com.gcox.fansmeet.domain.interactors.celebrity

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.CelebrityRepository
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by ngoc on 5/18/18.
 */
class GetCelebrityProfileUseCase constructor(
    uiThread: Scheduler,
    executorThread: Scheduler,
    private val dataRepository: CelebrityRepository
) : UseCase<CelebrityProfileModel, GetCelebrityProfileUseCase.Params>(uiThread, executorThread) {

    override fun buildObservable(params: Params): Observable<CelebrityProfileModel> {
        return dataRepository.getCelebrityProfile(params.userId, params.username)
    }

    class Params(var userId: Int, var username: String) {
        companion object {
            @JvmStatic
            fun load(userId: Int, username: String): Params {
                return Params(userId, username)
            }
        }
    }
}