package com.gcox.fansmeet.features.profile.userprofile

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityProfileUseCase
import com.gcox.fansmeet.domain.interactors.topfans.TopFansUseCase
import com.gcox.fansmeet.domain.interactors.useraction.FollowUseCase
import com.gcox.fansmeet.domain.interactors.useraction.UnFollowUseCase
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.topfans.TopFanModel
import com.gcox.fansmeet.models.FollowUserModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class UserInfoViewModel constructor(
    private val getCelebrityProfileUseCase: GetCelebrityProfileUseCase,
    private val topFansUseCase: TopFansUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase
) : BaseViewModel() {

    private var celebrityProfileResponse = MutableLiveData<CelebrityProfileModel>()
    val getCelebrityProfile = celebrityProfileResponse

    private var topFansResponse = MutableLiveData<BaseDataPagingResponseModel<TopFanModel>>()
    val getTopFans = topFansResponse

    private var followUseResponse = MutableLiveData<FollowUserModel>()
    val followUse = followUseResponse

    private var unFollowUseResponse = MutableLiveData<FollowUserModel>()
    val unFollowUse = unFollowUseResponse

    fun getCelebrityProfile(userId: Int,userName:String) {
        runUseCase(getCelebrityProfileUseCase, GetCelebrityProfileUseCase.Params.load(userId,userName)) {
            celebrityProfileResponse.value = it
        }
    }

    fun getTopFans(userId: Int) {
        runUseCase(topFansUseCase, TopFansUseCase.Params.load(userId, Constants.WEB_SERVICE_START_PAGE, 4)) {
            topFansResponse.value = it.responseModel
        }
    }

    fun followUser(userId: Int){
        runUseCase(followUseCase, userId) {
            followUseResponse.value = it
        }
    }

    fun unFollowUser(userId: Int){
        runUseCase(unFollowUseCase, userId) {
            unFollowUseResponse.value = it
        }
    }
}