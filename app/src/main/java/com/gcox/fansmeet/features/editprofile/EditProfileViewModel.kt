package com.gcox.fansmeet.features.editprofile

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.editprofile.CheckEmailUseCase
import com.gcox.fansmeet.domain.interactors.editprofile.EditProfileUseCase
import com.gcox.fansmeet.webservice.response.EditProfileResponseModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import okhttp3.MultipartBody


/**
 * Created by Ngoc on 5/18/18.
 */
class EditProfileViewModel constructor(
    private val getStartUseCase: EditProfileUseCase,
    private val checkEmailUseCase: CheckEmailUseCase
) : BaseViewModel() {

    private var updateProfileResponse = MutableLiveData<SettingResponse>()
    val response = updateProfileResponse

    private var checkEmailResponse = MutableLiveData<Boolean>()
    val checkEmail = checkEmailResponse

    fun updateProfile(multipartBody: MultipartBody) {
        runUseCase(getStartUseCase, EditProfileUseCase.Params.load(multipartBody)) {
            updateProfileResponse.value = it
        }
    }

    fun checkEmailAvailable(email: String) {
        runUseCase(checkEmailUseCase, email) {
            checkEmailResponse.value = it
        }
    }

}
