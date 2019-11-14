package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityListUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityProfileUseCase
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel


/**
 * Created by Ngoc on 5/18/18.
 */
class CelebrityViewModel constructor(
    private val getCelebrityUseCase: GetCelebrityListUseCase,
    private val getCelebrityProfileUseCase: GetCelebrityProfileUseCase
) : BaseViewModel() {

    private var celebrityResponse = MutableLiveData<CelebrityResponse>()
    val getCelebrity = celebrityResponse

    private var celebrityProfileResponse = MutableLiveData<CelebrityProfileModel>()
    val getCelebrityProfile = celebrityProfileResponse

    fun getCelebrityList(userId:Int,username:String, nextId:Int) {
        runUseCase(getCelebrityUseCase, CelebrityRequestModel(userId,username, nextId, Constants.PAGE_LIMITED)) {
            celebrityResponse.value = it
        }
    }

    fun getCelebrityProfile(userId:Int){
//        runUseCase(getCelebrityProfileUseCase, userId,) {
//            celebrityProfileResponse.value = it
//        }
    }
}
