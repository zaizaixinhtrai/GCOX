package com.gcox.fansmeet.features.register;

import com.gcox.fansmeet.webservice.request.RegisterWithGoogleRequestModel;
import com.gcox.fansmeet.features.mvpbase.BaseContract;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.webservice.request.RegisterWithFacebookRequestModel;
import com.gcox.fansmeet.webservice.request.RegisterWithInstagramRequestModel;

/**
 * Created by ThanhBan on 11/14/2016.
 */

public interface RegisterContract {
    interface RegisterView extends BaseContract.View {
        void userIdAvailable();
        void userIdInAvailable();
        void onGetUserIdSuggestionSuccessfully(String suggestedUserId);

        void enableBeginButton(boolean enable);
        void onUserRegisterCompleted(UserModel userInforModel,
                                     String userToken);
        void onAdminBlocked(String message);

        void emailAvailable(boolean available);
        void referralCodeAvailable(boolean available);
    }

    interface UserActions extends BaseContract.Presenter<RegisterView> {
        void checkUserNameAvailable(String userId);
        void getUserIdSuggestion(String expectedUserId);
        void getUserIdSuggestion();

        void register(RegisterWithGoogleRequestModel requestModel);

        void register(RegisterWithFacebookRequestModel requestModel);

        void register(RegisterWithInstagramRequestModel requestModel);

        void checkReferralCodeAvailable(String referralCode);
        void checkEmailAvailable(String email);

    }
}
