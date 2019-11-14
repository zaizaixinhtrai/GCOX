package com.gcox.fansmeet.features.login

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
import timber.log.Timber
import java.sql.Time


class LoginViewModel(
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

    fun registerWithGoogle(requestModel: RegisterWithGoogleRequestModel) {

        val disposable = Single.just(AppsterWebServices.get().registerWithGoogle(requestModel.build())
            .subscribe({ response ->
                if (response != null && response.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                    registerInSuccess(response.data)
                }
            }, { throwable ->
            })
        )
        compositeDisposable.add(disposable.subscribe())
    }


    fun registerWithFacebook(requestModel: RegisterWithFacebookRequestModel) {
        val disposable = Single.just(AppsterWebServices.get().registerWithFacebook(requestModel.build())
            .subscribe({ response ->
                if (response != null && response.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                    registerInSuccess(response.data)
                }
            }, { throwable ->
            })
        )
        compositeDisposable.add(disposable.subscribe())
    }

    fun registerWithInstagram(requestModel: RegisterWithInstagramRequestModel) {
        val disposable = Single.just(AppsterWebServices.get().registerWithInstagram(requestModel.build())
            .subscribe({ response ->
                if (response != null && response.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                    registerInSuccess(response.data)
                }
            }, { throwable ->
            })
        )
        compositeDisposable.add(disposable.subscribe())
    }

    fun disposableComposite() {
        compositeDisposable.dispose()
    }

    private fun onLoggedInSuccess(rsp: LoginResponseModel) {
        AppsterApplication.mAppPreferences.saveUserInforModel(rsp.userInfo)
        AppsterApplication.mAppPreferences.saveUserToken(rsp.accessToken)
        loginLiveData.value = rsp.userInfo
        OneSignalUtil.setUser(rsp.userInfo)

//        AppsterApplication.application?.startKoin()

        Timber.e("TOLKEN+++ "+ rsp.accessToken)
    }

    private fun registerInSuccess(rsp: RegisterWithFacebookResponseModel) {
        AppsterApplication.mAppPreferences.saveUserInforModel(rsp.userInfo)
        AppsterApplication.mAppPreferences.saveUserToken(rsp.access_token)
        loginLiveData.value = rsp.userInfo
    }

    enum class LoginType {
        GOOGLE, FACEBOOK, INSTAGRAM
    }
}