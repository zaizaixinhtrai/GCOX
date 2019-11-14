package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.models.PostDataModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody

/**
 * Created by ngoc on 5/18/18.
 */
interface PostRepository{

     fun createPost(isEditing: Boolean,isSubmissionChallenge: Boolean, challengeId: Int, multipartBody: MultipartBody): Observable<BaseResponse<PostDataModel>>

     fun createChallenge(multipartBody: MultipartBody,isEdit:Boolean,challengeId:Int): Observable<BaseResponse<PostDataModel>>

     fun deleteChallenge(challengeId: Int,type:Int): Observable<Boolean>

}