package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.EditProfileResponseModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by ngoc on 5/18/18.
 */
interface EditProfileRepository{

     fun editProfile(multipartBody: MultipartBody): Observable<SettingResponse>

     fun isEmailExist(email:String):Observable<Boolean>

}