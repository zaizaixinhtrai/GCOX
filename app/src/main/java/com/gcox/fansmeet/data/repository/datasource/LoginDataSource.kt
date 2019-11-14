package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.webservice.request.GoogleLoginRequestModel
import com.gcox.fansmeet.webservice.request.InstagramLoginRequestModel
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel
import com.gcox.fansmeet.webservice.request.TwitterLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable

interface LoginDataSource {
    fun loginWithFacebook(request: LoginFacebookRequestModel): Observable<BaseResponse<LoginResponseModel?>>
    fun loginWithGoogle(request: GoogleLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>
    fun loginWithInstagram(request: InstagramLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>
}
