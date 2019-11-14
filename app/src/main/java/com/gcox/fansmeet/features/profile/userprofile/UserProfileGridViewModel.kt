package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityGridUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityListUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityProfileUseCase
import com.gcox.fansmeet.features.editvideo.CONSTANTS
import com.gcox.fansmeet.webservice.request.CelebrityRequestModel


/**
 * Created by Ngoc on 5/18/18.
 */
class UserProfileGridViewModel constructor(
    private val getCelebrityGridUseCase: GetCelebrityGridUseCase
) : BaseViewModel() {

    private var celebrityResponse = MutableLiveData<CelebrityGridResponse>()
    val getCelebrityGrid = celebrityResponse

    fun getCelebrityGrid(userId: Int,username:String, nextId: Int, isRefresh: Boolean) {
        runUseCase(getCelebrityGridUseCase, CelebrityRequestModel(userId,username, nextId, Constants.ITEM_PAGE_LIMITED_FOR_GRID)) {
            it.isRefresh = isRefresh
            celebrityResponse.value = it
        }
    }
}
