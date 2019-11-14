package com.gcox.fansmeet.domain.interactors.login


import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.UserLoginRepository
import com.gcox.fansmeet.webservice.request.InstagramLoginRequestModel
import com.gcox.fansmeet.webservice.request.TwitterLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class InstagramLoginUseCase
constructor(uiThread: Scheduler, executorThread: Scheduler, private val loginRepository: UserLoginRepository) :
    UseCase<BaseResponse<LoginResponseModel?>, InstagramLoginRequestModel>(uiThread, executorThread) {
    override fun buildObservable(params: InstagramLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>> =
        loginRepository.loginWithInstagram(params)
}