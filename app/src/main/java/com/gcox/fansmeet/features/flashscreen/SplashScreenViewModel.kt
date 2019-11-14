package com.gcox.fansmeet.features.flashscreen

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.login.FacebookLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.GoogleLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.InstagramLoginUseCase
import com.gcox.fansmeet.models.UserModel
import com.gcox.fansmeet.pushnotification.OneSignalUtil
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.gcox.fansmeet.webservice.request.*
import com.gcox.fansmeet.webservice.response.LoginResponseModel
import com.gcox.fansmeet.webservice.response.RegisterWithFacebookResponseModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class SplashScreenViewModel(
    private val facebookLoginUseCase: FacebookLoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val twitterLoginUseCase: InstagramLoginUseCase
) : BaseViewModel() {

    private var loginLiveData = MutableLiveData<UserModel>()
    private var compositeDisposable = CompositeDisposable()
     var loginType: LoginType?=null

    val getLoginData = loginLiveData

    fun loginWithFacebook(requestLogin: LoginFacebookRequestModel?) {
        loginType = LoginType.FACEBOOK
        runUseCaseWithBaseResponse(facebookLoginUseCase, requestLogin) {
            onLoggedInSuccess(it!!)
        }
    }

    fun loginWithGoogle(requestLogin: GoogleLoginRequestModel) {
        loginType = LoginType.GOOGLE
        runUseCaseWithBaseResponse(googleLoginUseCase, requestLogin) {
            onLoggedInSuccess(it!!)
        }
    }

    fun loginWithInstagram(instagramLoginRequestModel: InstagramLoginRequestModel) {
        loginType = LoginType.INSTAGRAM
        runUseCaseWithBaseResponse(twitterLoginUseCase, instagramLoginRequestModel) {
            onLoggedInSuccess(it!!)
        }
    }

    fun disposableComposite() {
       if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    private fun onLoggedInSuccess(rsp: LoginResponseModel) {
        AppsterApplication.mAppPreferences.saveUserInforModel(rsp.userInfo)
        AppsterApplication.mAppPreferences.saveUserToken(rsp.accessToken)
        loginLiveData.value = rsp.userInfo
    }

    private fun registerInSuccess(rsp: RegisterWithFacebookResponseModel) {
        AppsterApplication.mAppPreferences.saveUserInforModel(rsp.userInfo)
        AppsterApplication.mAppPreferences.saveUserToken(rsp.access_token)
        loginLiveData.value = rsp.userInfo
        OneSignalUtil.setUser(rsp.userInfo)
    }

    enum class LoginType {
        GOOGLE, FACEBOOK, INSTAGRAM
    }
}