package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.repository.datasource.EditProfileDataSource
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.EditProfileResponseModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by linh on 11/10/2017.
 */

class CloudEditProfileDataSource constructor(private val mService: AppsterWebserviceAPI) :
    EditProfileDataSource {

    override fun updateProfile(multipartBody: MultipartBody): Observable<BaseResponse<SettingResponse>> {
        return  mService.updateProfile(AppsterUtility.getAuth(), multipartBody)
    }

    override fun isEmailExist(email: String): Observable<BaseResponse<Boolean>> {
        return mService.isEmailExist(email)
    }

//    override fun updateProfile(multipartBody: MultipartBody?): Observable<BaseResponse<SettingResponse>> {
//        return  mService.updateProfile(mAuthen, multipartBody)
//    }
}
