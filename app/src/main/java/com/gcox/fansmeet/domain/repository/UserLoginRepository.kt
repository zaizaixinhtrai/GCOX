package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.webservice.request.GoogleLoginRequestModel
import com.gcox.fansmeet.webservice.request.InstagramLoginRequestModel
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel
import com.gcox.fansmeet.webservice.request.TwitterLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable

/**
 * Created by gyben on 5/10/18.
 */
interface UserLoginRepository {
    fun loginWithFacebook(request: LoginFacebookRequestModel): Observable<BaseResponse<LoginResponseModel?>>
    fun loginWithGoogle(request: GoogleLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>
    fun loginWithInstagram(request: InstagramLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>>
}
