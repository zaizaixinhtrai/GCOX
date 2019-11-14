package com.gcox.fansmeet.domain.interactors.login


import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.UserLoginRepository
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class FacebookLoginUseCase
constructor(uiThread: Scheduler, executorThread: Scheduler, private val loginRepository: UserLoginRepository) :
    UseCase<BaseResponse<LoginResponseModel?>, LoginFacebookRequestModel>(uiThread, executorThread) {
    override fun buildObservable(params: LoginFacebookRequestModel): Observable<BaseResponse<LoginResponseModel?>> =
        loginRepository.loginWithFacebook(params)
}