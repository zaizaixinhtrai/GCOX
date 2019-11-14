package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.EditProfileResponseModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by ngoc on 11/10/2017.
 */

interface EditProfileDataSource {
    fun updateProfile(multipartBody: MultipartBody): Observable<BaseResponse<SettingResponse>>

    fun isEmailExist(email: String): Observable<BaseResponse<Boolean>>
}
