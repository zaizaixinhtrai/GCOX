package com.gcox.fansmeet.data.repository.datasource.remote

import com.gcox.fansmeet.data.repository.datasource.LoginDataSource
import com.gcox.fansmeet.models.UserModel
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.GoogleLoginRequestModel
import com.gcox.fansmeet.webservice.request.InstagramLoginRequestModel
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel
import com.gcox.fansmeet.webservice.request.TwitterLoginRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import io.reactivex.Observable

class CloudLoginDataSource
constructor(private val service: AppsterWebserviceAPI) : LoginDataSource {

    override fun loginWithFacebook(request: LoginFacebookRequestModel): Observable<BaseResponse<LoginResponseModel?>> {
        return service.loginWithFacebook(request)
    }

    override fun loginWithGoogle(request: GoogleLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>> {
        return service.loginWithGoogle(request)
    }

    override fun loginWithInstagram(request: InstagramLoginRequestModel): Observable<BaseResponse<LoginResponseModel?>> {
        return service.loginWithInstagram(request)
    }

    private fun fakeLoginRsp(): Observable<BaseResponse<LoginResponseModel?>>{
        return Observable.fromCallable {
            val rsp = BaseResponse<LoginResponseModel?>()
            val loginRsp = LoginResponseModel()
            val userModel = UserModel()
            userModel.userId = 4477
            userModel.displayName = "Nguyễn Văn Thị"
            userModel.userName = "anhvlvlvl"
            userModel.email ="nguyenvanthi@gmail.com"

            loginRsp.userInfo = userModel
            loginRsp.accessToken = "lknfofpkafpoefmwerop2k45205koptjmo2"
            rsp.data =loginRsp
            rsp.code = 1
            rsp
        }
    }

    private fun fakeFailedLoginRsp(): Observable<BaseResponse<LoginResponseModel?>>{
        return Observable.fromCallable {
            val rsp = BaseResponse<LoginResponseModel?>()
            rsp.code = -1
            rsp.message = "Error"
            rsp
        }
    }
}
