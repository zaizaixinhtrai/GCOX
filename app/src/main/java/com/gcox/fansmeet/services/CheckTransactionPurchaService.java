package com.gcox.fansmeet.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import java.util.ArrayList;

/**
 * Created by linh on 21/06/2017.
 */

public class CheckTransactionPurchaService extends IntentService {
    CompositeDisposable mCompositeSubscription;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public CheckTransactionPurchaService() {
        super("RefreshFollowerListService");
        Timber.d("RefreshFollowerListService constructor");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.e("onHandleIntent");
        if (intent == null) {
            return;
        }

        mCompositeSubscription = new CompositeDisposable();
//        VerifyIAPRequestModel model = AppsterApplication.mAppPreferences.getVerifyIAPRequestModel();
//        if (model != null) {
//            verifyPurchasedWithServer(model);
//        }

        ArrayList<VerifyIAPRequestModel> orderIds = AppsterApplication.mAppPreferences.loadOrderObjectToList();
        if (orderIds != null) {
            for (int i = 0; i < orderIds.size(); i++) {
                verifyPurchasedWithServer(orderIds.get(i));
            }
        }
    }

    public void verifyPurchasedWithServer(VerifyIAPRequestModel requestModel) {
        if (!DialogManager.isShowing()) {
            DialogManager.getInstance().showDialog(this, this.getResources().getString(R.string.connecting_msg));
        }

        mCompositeSubscription.add(
                AppsterWebServices.get().verifyIAPPurchased(AppsterApplication.mAppPreferences.getUserTokenRequest(),
                        requestModel)
                        .subscribe(verifyIAPResponeModel -> {
                            DialogManager.getInstance().dismisDialog();
                            if (verifyIAPResponeModel == null) return;
                            if (verifyIAPResponeModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                                Timber.e("Bean =" + verifyIAPResponeModel.getData().getTotalBeanIncrease());
                                AppsterApplication.mAppPreferences.getUserModel().setGems(verifyIAPResponeModel.getData().getTotalBeanIncrease());
                                AppsterApplication.mAppPreferences.removeOrderObjectToList(requestModel);
                            }
                        }, error -> {
                            DialogManager.getInstance().dismisDialog();
                            Timber.e(error.getMessage());
                        }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isDisposed()) mCompositeSubscription.dispose();
    }

}
