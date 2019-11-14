package com.gcox.fansmeet.domain.interactors.editprofile

import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.data.repository.PostDataRepository
import com.gcox.fansmeet.domain.interactors.topfans.TopFansUseCase
import com.gcox.fansmeet.domain.repository.EditProfileRepository
import com.gcox.fansmeet.domain.repository.PostRepository
import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.EditProfileResponseModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import okhttp3.MultipartBody

/**
 * Created by ngoc on 10/10/2017.
 */

class CheckEmailUseCase constructor(
    uiThread: Scheduler, executorThread: Scheduler, private val mDataRepository: EditProfileRepository
) : UseCase<Boolean, String>(uiThread, executorThread) {

    override fun buildObservable(email: String): Observable<Boolean> {
        return mDataRepository.isEmailExist(email)
    }


}
