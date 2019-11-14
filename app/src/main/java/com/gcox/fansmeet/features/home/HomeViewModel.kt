package com.gcox.fansmeet.features.home

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.home.GetBannersUseCase
import com.gcox.fansmeet.domain.interactors.home.GetUsersUseCase
import com.gcox.fansmeet.domain.interactors.useraction.FollowUseCase
import com.gcox.fansmeet.domain.interactors.useraction.UnFollowUseCase
import com.gcox.fansmeet.webservice.request.HomeCelebritiesRequestModel


/**
 * Created by ngoc on 5/18/18.
 */
class HomeViewModel constructor(
    private val getLiveShowsUseCase: GetUsersUseCase,
    private val getBannersUseCase: GetBannersUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase
) : BaseViewModel() {

    private var homeData = MutableLiveData<HomeModel>()
    private var banners = MutableLiveData<List<String>>()
    val getHomeData = homeData
    val getBanners = banners
    private var followUseResponse = MutableLiveData<FollowResponse>()
    val followUse = followUseResponse

    private var unFollowUseResponse = MutableLiveData<FollowResponse>()
    val unFollowUse = unFollowUseResponse

    fun getUsers(type: Int, nextId: Int) {
        runUseCase(getLiveShowsUseCase, HomeCelebritiesRequestModel(nextId, Constants.PAGE_LIMITED, type)) {
            homeData.value = it
        }
    }

    fun getBanners() {
        runUseCaseWithBaseResponse(getBannersUseCase, Unit) {
            banners.value = it
        }
    }

    fun followUser(userId: Int, position: Int) {
        runUseCase(followUseCase, userId) {
            followUseResponse.value = FollowResponse(userId, position)
            if (it.meFollowingCount !=null) AppsterApplication.mAppPreferences.userModel.followingCount = it.meFollowingCount!!
        }
    }

    fun unFollowUser(userId: Int, position: Int) {
        runUseCase(unFollowUseCase, userId) {
            unFollowUseResponse.value = FollowResponse(userId, position)
            if (it.meFollowingCount !=null) AppsterApplication.mAppPreferences.userModel.followingCount = it.meFollowingCount!!
        }
    }

    fun clearData() {
        getHomeData.value = null
        getBanners.value = null
        followUse.value = null
        unFollowUse.value =null
        onCleared()
    }
}
