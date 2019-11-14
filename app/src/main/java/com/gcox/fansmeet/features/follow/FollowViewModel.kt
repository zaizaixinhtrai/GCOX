package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.follow.GetFollowListUseCase
import com.gcox.fansmeet.domain.interactors.post.CreateChallengePostUseCase
import com.gcox.fansmeet.domain.interactors.useraction.FollowUseCase
import com.gcox.fansmeet.domain.interactors.useraction.UnFollowUseCase
import com.gcox.fansmeet.features.home.FollowResponse
import com.gcox.fansmeet.models.FollowItemModel
import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import okhttp3.MultipartBody

/**
 * Created by Ngoc on 5/18/18.
 */
class FollowViewModel constructor(
    private val getFollowListUseCase: GetFollowListUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase
) : BaseViewModel() {

    private var followListResponse = MutableLiveData<BaseDataPagingResponseModel<FollowItemModel>>()
    val getFollowListResponse = followListResponse

    private var followUseResponse = MutableLiveData<FollowResponse>()
    val followUse = followUseResponse

    private var unFollowUseResponse = MutableLiveData<FollowResponse>()
    val unFollowUse = unFollowUseResponse

    fun getFollowList(userId: Int, nextId: Int, type: Int) {
        runUseCase(
            getFollowListUseCase,
            GetFollowListUseCase.Params.load(userId, nextId, Constants.PAGE_LIMITED, type)
        ) {
            followListResponse.value = it
        }
    }

    fun followUser(userId: Int, position: Int) {
        runUseCase(followUseCase, userId) {
            followUseResponse.value = FollowResponse(userId, position)
            if (it.meFollowingCount != null) AppsterApplication.mAppPreferences.userModel.followingCount =
                it.meFollowingCount!!
        }
    }

    fun unFollowUser(userId: Int, position: Int) {
        runUseCase(unFollowUseCase, userId) {
            unFollowUseResponse.value = FollowResponse(userId, position)
            if (it.meFollowingCount != null) AppsterApplication.mAppPreferences.userModel.followingCount =
                it.meFollowingCount!!
        }
    }

}
