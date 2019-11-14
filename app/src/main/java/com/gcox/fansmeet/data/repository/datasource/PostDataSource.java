package com.gcox.fansmeet.data.repository.datasource;

import com.gcox.fansmeet.models.PostDataModel;
import com.gcox.fansmeet.webservice.response.BaseResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;

/**
 * Created by linh on 11/10/2017.
 */

public interface PostDataSource {
    Observable<BaseResponse<PostDataModel>> createPost(boolean isEditing,boolean isSubmissionChallenge, int challengeId, MultipartBody multipartBody);

    Observable<BaseResponse<PostDataModel>> createChallenge(MultipartBody multipartBody,Boolean isEdit,Integer challengeId);

    Observable<BaseResponse<Boolean>> deleteChallenge(int challengeId,int type);
}
