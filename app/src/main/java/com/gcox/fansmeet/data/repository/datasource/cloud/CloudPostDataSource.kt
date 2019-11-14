package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.repository.datasource.PostDataSource
import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.request.PostCreatePostRequestModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by linh on 11/10/2017.
 */

class CloudPostDataSource constructor(private val mService: AppsterWebserviceAPI) :
    PostDataSource {
    override fun deleteChallenge(challengeId: Int, postType: Int): Observable<BaseResponse<Boolean>> {
        return if (postType == Constants.CHALLENGE_SUBMISSION) {
            mService.deleteSubmission(AppsterUtility.getAuth(), challengeId)
        } else
            mService.deleteChallenge(AppsterUtility.getAuth(), challengeId)
    }

    override fun createChallenge(
        multipartBody: MultipartBody?,
        isEdit: Boolean,
        challengeId: Int
    ): Observable<BaseResponse<PostDataModel>> {
        return if (isEdit) {
            mService.editChallenge(AppsterUtility.getAuth(), challengeId, multipartBody)
        } else {
            mService.createChallenge(AppsterUtility.getAuth(), multipartBody)
        }
    }

    override fun createPost(
        isEditing: Boolean,
        isSubmissionChallenge: Boolean,
        challengeId: Int,
        multipartBody: MultipartBody
    ): Observable<BaseResponse<PostDataModel>> {
        return if (isEditing) {
            if (isSubmissionChallenge) {
                mService.editSubmission(AppsterUtility.getAuth(), challengeId, multipartBody)
            } else {
                mService.editPost(AppsterUtility.getAuth(), challengeId, multipartBody)
            }
        } else {
            if (isSubmissionChallenge) {
                mService.submissionChallenge(AppsterUtility.getAuth(), challengeId, multipartBody)
            } else {
                mService.postCreatePost(AppsterUtility.getAuth(), multipartBody)
            }
        }
    }
}
