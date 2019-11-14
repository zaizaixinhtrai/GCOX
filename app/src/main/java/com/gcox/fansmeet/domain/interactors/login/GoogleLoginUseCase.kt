package com.gcox.fansmeet.domain.interactors.login


import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.domain.repository.UserLoginRepository
import com.gcox.fansmeet.webservice.request.GoogleLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class GoogleLoginUseCase
constructor(uiThread: Scheduler, executorThread: Scheduler, private val loginRepository: UserLoginRepository) :
    UseCase<BaseResponse<LoginResponseModel?>, GoogleLoginRequestModel>(uiThread, executorThread) {
    override fun buildObservable(params: GoogleLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>> =
        loginRepository.loginWithGoogle(params)
}