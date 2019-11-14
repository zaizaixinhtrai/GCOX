package com.gcox.fansmeet.features.register;

import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.pushnotification.OneSignalUtil;
import com.gcox.fansmeet.webservice.request.RegisterWithGoogleRequestModel;
import com.gcox.fansmeet.manager.ShowErrorManager;
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI;
import com.gcox.fansmeet.webservice.request.RegisterWithFacebookRequestModel;
import com.gcox.fansmeet.webservice.request.RegisterWithInstagramRequestModel;
import com.gcox.fansmeet.webservice.response.BaseResponse;
import com.gcox.fansmeet.webservice.response.RegisterWithFacebookResponseModel;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


/**
 * Created by Ngoc on 11/14/2016.
 */

public class RegisterPresenter implements RegisterContract.UserActions {

    private RegisterContract.RegisterView mView;
    private AppsterWebserviceAPI mService;
    private CompositeDisposable compositeDisposable;

    public RegisterPresenter(RegisterContract.RegisterView pView, AppsterWebserviceAPI pService) {
        attachView(pView);
        this.mService = pService;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void checkUserNameAvailable(String userName) {

        compositeDisposable.add(mService.verifyUsername(userName)
                .filter(verifyUsernameDataResponse -> verifyUsernameDataResponse != null && mView != null)
                .subscribe(verifyUsernameDataResponse -> {
                    if (verifyUsernameDataResponse.getCode() != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
//                        mView.loadError(verifyUsernameDataResponse.getMessage(), verifyUsernameDataResponse.getCode());
                    } else {
                        if (!verifyUsernameDataResponse.getData()) {
                            mView.userIdAvailable();
                        } else {
                            mView.userIdInAvailable();
                        }
                    }
                }, error -> {
                    if (mView != null) {
//                        mView.loadError(error.getMessage(), Constants.RETROFIT_ERROR);
                    }
                    Timber.d(error.getMessage());
                }));
    }

    @Override
    public void getUserIdSuggestion(String expectedUserId) {
//        mCompositeSubscription.add(mService.getSuggestedUserId(mAuthen, expectedUserId)
//        .subscribe(baseResponse -> mView.onGetUserIdSuggestionSuccessfully(baseResponse.getData()), Timber::e));
    }

    @Override
    public void getUserIdSuggestion() {
//        mCompositeSubscription.add(mService.getSuggestedUserId(mAuthen)
//                .subscribe(baseResponse -> mView.onGetUserIdSuggestionSuccessfully(baseResponse.getData()), Timber::e));
    }


    @Override
    public void register(RegisterWithGoogleRequestModel requestModel) {
        mView.showProgress();
        compositeDisposable.add(mService.registerWithGoogle(requestModel.build())
                .subscribe(this::onCreateProfileResponse, throwable -> {
                    mView.hideProgress();
                    Timber.e(throwable.getMessage());
                    mView.loadError(throwable.getMessage(), Constants.RETROFIT_ERROR);
                }));
    }

    @Override
    public void register(RegisterWithFacebookRequestModel requestModel) {
        mView.showProgress();
        compositeDisposable.add(mService.registerWithFacebook(requestModel.build())
                .subscribe(this::onCreateProfileResponse, throwable -> {
                    mView.hideProgress();
                    Timber.e(throwable.getMessage());
                    mView.loadError(throwable.getMessage(), Constants.RETROFIT_ERROR);
                }));
    }

    @Override
    public void register(RegisterWithInstagramRequestModel requestModel) {
        mView.showProgress();
        compositeDisposable.add(mService.registerWithInstagram(requestModel.build())
                .subscribe(this::onCreateProfileResponse, throwable -> {
                    mView.hideProgress();
                    Timber.e(throwable.getMessage());
                    mView.loadError(throwable.getMessage(), Constants.RETROFIT_ERROR);
                }));
    }

    @Override
    public void checkReferralCodeAvailable(String referralCode) {
        compositeDisposable.add(mService.isReferralCodeExist(referralCode)
                .filter(verifyUsernameDataResponse -> verifyUsernameDataResponse != null && mView != null)
                .subscribe(verifyUsernameDataResponse -> {
                    if (verifyUsernameDataResponse.getCode() != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                    } else {
                        mView.referralCodeAvailable(verifyUsernameDataResponse.getData());
                    }
                }, error -> Timber.e(error.getMessage())));
    }

    @Override
    public void checkEmailAvailable(String email) {
        compositeDisposable.add(mService.isEmailExist(email)
                .filter(verifyUsernameDataResponse -> verifyUsernameDataResponse != null && mView != null)
                .subscribe(verifyUsernameDataResponse -> {
                    if (verifyUsernameDataResponse.getCode() != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                    } else {
                        mView.emailAvailable(verifyUsernameDataResponse.getData());
                    }
                }, error -> Timber.e(error.getMessage())));
    }

    @Override
    public void attachView(RegisterContract.RegisterView view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    private void onCreateProfileResponse(BaseResponse<RegisterWithFacebookResponseModel> registerResponse) {
        mView.hideProgress();
        if (registerResponse == null) return;
        if (registerResponse.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {

            mView.onUserRegisterCompleted(registerResponse.getData().getUserInfo(),
                    registerResponse.getData().getAccess_token());
            OneSignalUtil.setUser(registerResponse.getData().getUserInfo());
        } else if (registerResponse.getCode() == ShowErrorManager.ADMIN_BLOCKED) {
            mView.onAdminBlocked(registerResponse.getMessage());

        } else {
            mView.loadError(registerResponse.getMessage(), registerResponse.getCode());
        }
    }
}
