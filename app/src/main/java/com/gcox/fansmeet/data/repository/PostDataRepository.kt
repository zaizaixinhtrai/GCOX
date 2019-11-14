package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.repository.datasource.PostDataSource
import com.gcox.fansmeet.domain.repository.PostRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by linh on 11/10/2017.
 */

class PostDataRepository constructor(@Remote private val mCloudDataSource: PostDataSource) : PostRepository {

    override fun deleteChallenge(challengeId: Int, type: Int): Observable<Boolean> {

        return mCloudDataSource.deleteChallenge(challengeId, type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun createChallenge(
        multipartBody: MultipartBody,
        isEdit: Boolean,
        challengeId: Int
    ): Observable<BaseResponse<PostDataModel>> {
        return mCloudDataSource.createChallenge(multipartBody, isEdit, challengeId)
    }

    override fun createPost(
        isEditing: Boolean,
        isSubmissionChallenge: Boolean,
        challengeId: Int,
        multipartBody: MultipartBody
    ): Observable<BaseResponse<PostDataModel>> {
        return mCloudDataSource.createPost(isEditing, isSubmissionChallenge, challengeId, multipartBody)
    }
}
