package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.data.repository.datasource.LoginDataSource
import com.gcox.fansmeet.domain.repository.UserLoginRepository
import com.gcox.fansmeet.webservice.request.GoogleLoginRequestModel
import com.gcox.fansmeet.webservice.request.InstagramLoginRequestModel
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel
import com.gcox.fansmeet.webservice.request.TwitterLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable

class UserLoginDataRepository constructor(@Remote private val loginDataSource: LoginDataSource): UserLoginRepository {

    override fun loginWithFacebook(request: LoginFacebookRequestModel): Observable<BaseResponse<LoginResponseModel?>> = loginDataSource.loginWithFacebook(request)
    override fun loginWithGoogle(request: GoogleLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>  = loginDataSource.loginWithGoogle(request)
    override fun loginWithInstagram(request: InstagramLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>  = loginDataSource.loginWithInstagram(request)
}