package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.repository.datasource.EditProfileDataSource
import com.gcox.fansmeet.domain.repository.EditProfileRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.webservice.response.SettingResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by ngoc on 11/10/2017.
 */

class EditProfileDataRepository constructor(@Remote private val mCloudDataSource: EditProfileDataSource) :
    EditProfileRepository {

    override fun isEmailExist(email: String): Observable<Boolean> {
        return mCloudDataSource.isEmailExist(email).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun editProfile(multipartBody: MultipartBody): Observable<SettingResponse> {
        return mCloudDataSource.updateProfile(multipartBody).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

}
