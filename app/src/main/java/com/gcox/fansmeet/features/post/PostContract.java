package com.gcox.fansmeet.features.post;

import com.gcox.fansmeet.features.mvpbase.BaseContract;
import com.gcox.fansmeet.models.PostDataModel;
import okhttp3.MultipartBody;

/**
 * Created by linh on 10/10/2017.
 */

public class PostContract {
    interface View extends BaseContract.View {
        void onPostSuccessfully(PostDataModel model);
        void onPostFailed();
    }

    interface PostActions extends BaseContract.Presenter<View> {
        void post(boolean isEditing,boolean isSubmissionChallenge,int challengeId,MultipartBody multipartBody);
    }
}
