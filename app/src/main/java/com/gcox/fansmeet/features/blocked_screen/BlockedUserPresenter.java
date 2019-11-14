package com.gcox.fansmeet.features.blocked_screen;


import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.util.RxUtils;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI;
import com.gcox.fansmeet.webservice.request.BasePagingRequestModel;
import com.gcox.fansmeet.webservice.request.UnblockUserRequestModel;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by linh on 27/12/2016.
 */

public class BlockedUserPresenter implements BlockedScreenContract.UserActions {
    private AppsterWebserviceAPI mService;
    private BlockedScreenContract.BlockedUserView view;
    private String auth;
    private int offset = Constants.WEB_SERVICE_START_PAGE;
    boolean isEndList = false;
    private CompositeDisposable compositeDisposable;

    public BlockedUserPresenter(BlockedScreenContract.BlockedUserView view, AppsterWebserviceAPI service) {
        attachView(view);
        this.mService = service;
        auth = AppsterApplication.mAppPreferences.getUserTokenRequest();
        compositeDisposable = new CompositeDisposable();
    }

    //===========
    @Override
    public void attachView(BlockedScreenContract.BlockedUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    void getBlockedUsers() {
        if (isEndList) {
            return;
        }
        view.showProgress();
        compositeDisposable.add(AppsterWebServices.get().getBlockedUsers(auth, offset,Constants.PAGE_LIMITED)
                .subscribe(blockedUserResponse -> {
                    view.hideProgress();
                    if (blockedUserResponse == null) {
                        view.loadError("No data response!!", 9999);
                    } else {
                        if (blockedUserResponse.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                            view.onBlockedListResponse(blockedUserResponse.getData().getResult());
                            offset = blockedUserResponse.getData().getNextId();
                            isEndList = blockedUserResponse.getData().isEnd();
                        }
                    }
                }, throwable -> {
                    view.hideProgress();
                    view.loadError(throwable.getMessage(), Constants.RETROFIT_ERROR);
                }));
    }

    void unblockUser(BlockedUserModel usersItemModel, int position) {
        compositeDisposable.add(AppsterWebServices.get().unblockUser("Bearer " + AppsterApplication.mAppPreferences.getUserToken(), usersItemModel.getUserId())
                .subscribe(reportUserResponseModel -> {
                    if (reportUserResponseModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        view.onUnblockUserSuccessfully(position);
                    } else {
                        view.loadError(reportUserResponseModel.getMessage(), Constants.RETROFIT_ERROR);
                    }
                }, throwable -> view.loadError(throwable.getMessage(), Constants.RETROFIT_ERROR)));
    }
}
