package com.gcox.fansmeet.features.profile.celebrityprofile.delegates

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.post.CreateChallengePostUseCase
import com.gcox.fansmeet.domain.interactors.post.CreatePostUseCase
import com.gcox.fansmeet.models.PostDataModel
import okhttp3.MultipartBody

/**
 * Created by Ngoc on 5/18/18.
 */
class PostChallengeViewModel constructor(
    private val createPostUseCase: CreateChallengePostUseCase
) : BaseViewModel() {

    private var postChallengeResponse = MutableLiveData<PostDataModel>()
    val getPostChallengeResponse = postChallengeResponse

    fun submissionChallenge(multipartBody: MultipartBody, isEdit: Boolean, challengeId: Int) {
        runUseCase(createPostUseCase, CreateChallengePostUseCase.Params.load(multipartBody, isEdit, challengeId)) {
            postChallengeResponse.value = it.data
        }
    }
}
