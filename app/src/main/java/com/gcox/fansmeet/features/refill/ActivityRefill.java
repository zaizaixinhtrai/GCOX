package com.gcox.fansmeet.features.refill;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.billing.BillingManager;
import com.gcox.fansmeet.billing.BillingProvider;
import com.gcox.fansmeet.billing.SkuRowData;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.activity.BaseToolBarActivity;
import com.gcox.fansmeet.customview.GridViewWithHeaderAndFooter;

import com.gcox.fansmeet.features.mvpbase.RecyclerItemCallBack;
import com.gcox.fansmeet.util.CheckNetwork;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.util.PixelUtil;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel;

import timber.log.Timber;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ActivityRefill extends BaseToolBarActivity implements BillingProvider, RecyclerItemCallBack<RefillListItem> {
    private GridViewWithHeaderAndFooter topup_gridview;
    private List<RefillListItem> mListItems = new ArrayList<>();
    private RefillClassAdapter topUpClassAdapter;
    private long totalGem;
    private String transactionId;
    private TextView txt_txt_bean;
    private Button btnInviteFriend;
    private ViewGroup mHeader;
    private BillingManager mBillingManager;
    private final UpdateListener mUpdateListener = new UpdateListener();
    List<SkuRowData> dataList = new ArrayList<>();
    boolean isIAPSetUpFinished = false;
    private boolean shouldAllowBack = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create and initialize BillingManager which talks to BillingLibrary
        mBillingManager = new BillingManager(this, mUpdateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBillingManager != null) mBillingManager.destroy();
    }

    @Override
    public int getLayoutContentId() {
        return R.layout.top_up;
    }

    @Override
    public void init() {
        intId();
        setTopBarTile(getString(R.string.refill_title));
        useAppToolbarBackButton();
        getEventClickBack().setOnClickListener(v -> onBackPressed());
        handleTurnoffMenuSliding();
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B));
        checkTransactionPurchase();
    }

    private void intId() {
        topup_gridview = findViewById(R.id.topup_gridview);
    }

    private void addHeaderAndFooter(long gem) {
        LayoutInflater inflater = getLayoutInflater();
        mHeader = (ViewGroup) inflater.inflate(R.layout.topup_header_listview, topup_gridview, false);
        txt_txt_bean = mHeader.findViewById(R.id.txt_bean);
        btnInviteFriend = mHeader.findViewById(R.id.btnInviteFriend);
        SpannableStringBuilder builder = new SpannableStringBuilder(mActivity.getResources().getString(R.string.refill_invite_and_get));

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.refill_gem_icon);
        drawable.setBounds(0, 0, PixelUtil.dpToPx(this, 15), PixelUtil.dpToPx(this, 15));
        builder.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE),
                builder.length() - 1, builder.length(), 0);
        builder.append(" 100");
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#a4f475")), builder.length() - 3, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnInviteFriend.setOnClickListener(v -> {

        });
        btnInviteFriend.setGravity(Gravity.CENTER);
        btnInviteFriend.setText(builder);

        DecimalFormat myFormatter = new DecimalFormat("#,###");
        txt_txt_bean.setText(myFormatter.format(gem).replace(".", ","));
        topup_gridview.addHeaderView(mHeader, null, false);

//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        mFooterView = layoutInflater.inflate(R.layout.topup_footer_listview, null);
//        topup_gridview.addFooterView(mFooterView);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onClickBack(View view) {
        finish();
    }

    private void checkTransactionPurchase() {
        ArrayList<VerifyIAPRequestModel> orderIds = AppsterApplication.mAppPreferences.loadOrderObjectToList();
        if (orderIds != null) {
            for (int i = 0; i < orderIds.size(); i++) {
                verifyPurchasedWithServer(orderIds.get(i));
            }
        }
    }

    public void CheckTopUp() {
        DialogManager.getInstance().showDialog(ActivityRefill.this, getResources().getString(R.string.connecting_msg));
        compositeDisposable.add(AppsterWebServices.get().getRefillList(AppsterApplication.mAppPreferences.getUserTokenRequest())
                .subscribe(refillDataResponseModel -> {
                    DialogManager.getInstance().dismisDialog();
                    if (refillDataResponseModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        totalGem = refillDataResponseModel.getData().getGems();
                        addHeaderAndFooter(totalGem);
                        mListItems = refillDataResponseModel.getData().getResult();
                        topUpClassAdapter = new RefillClassAdapter(ActivityRefill.this, mListItems, this);
                        topup_gridview.setAdapter(topUpClassAdapter);
                        AppsterApplication.mAppPreferences.getUserModel().setGems(totalGem);
                        if (isIAPSetUpFinished) {
                            handleManagerAndUiReady();
                        }
                    } else if (refillDataResponseModel.getCodeDetails() != null && refillDataResponseModel.getCodeDetails().size() > 0) {
                        handleError(refillDataResponseModel.getMessage(), refillDataResponseModel.getCodeDetails().get(0).getCode());
                    } else {
                        handleError(refillDataResponseModel.getMessage(), refillDataResponseModel.getCode());
                    }
                }, error -> {
                    DialogManager.getInstance().dismisDialog();
                    handleError(error.getMessage(), Constants.RETROFIT_ERROR);
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.e("onActivityResult");
        updateGem();
    }

    private void updateGem() {
        if (txt_txt_bean != null) {
//            txt_txt_bean.setText(String.valueOf(AppsterApplication.mAppPreferences.getUserModel().getGems()));
            DecimalFormat myFormatter = new DecimalFormat("#,###");
            txt_txt_bean.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getGems()).replace(".", ","));
        }
    }

    @Override
    public BillingManager getBillingManager() {
        return mBillingManager;
    }

    @Override
    public void onItemClicked(RefillListItem item, int position) {
        if (preventMultiClicks()) return;
        for (SkuRowData skuRowData : dataList) {
            if (skuRowData.getSku().equals(item.getIos_store_id())) {
                Timber.e("init purchase for %s", skuRowData.getSku());
                getBillingManager().initiatePurchaseFlow(skuRowData.getOriginalJson());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldAllowBack) {
            super.onBackPressed();
        }
    }

    /**
     * Handler to billing updates
     */
    private class UpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() {
            onBillingManagerSetupFinished();
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchaseList) {

            for (Purchase purchase : purchaseList) {
                Timber.e("onPurchasesUpdated %s", purchase.toString());
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                    // We should consume the purchase and fill up the tank once it was consumed
                    shouldAllowBack = false;
                    getBillingManager().consumeAsync(purchase.getPurchaseToken(), (billingResult, purchaseToken) -> {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            // Successfully consumed, so we apply the effects of the item in our
                            // game world's logic, which in our case means filling the gas tank a bit
                            Timber.e("Consumption successful. Provisioning. %s", purchaseToken);
                            VerifyIAPRequestModel requestModel = new VerifyIAPRequestModel();
                            requestModel.setStore_id(purchase.getSku());
                            requestModel.setDevice_type(Constants.ANDROID_DEVICE_TYPE);
                            requestModel.setPayload(purchase.getOriginalJson());
                            requestModel.setSignature(purchase.getSignature());
                            requestModel.orderId = purchase.getOrderId();
                            // save iap model
                            AppsterApplication.mAppPreferences.saveOrderObjectToList(requestModel);
                            //only verify with purchased state
                            verifyPurchasedWithServer(requestModel);
                        } else {
                            shouldAllowBack = true;
                        }

                        Timber.e("End consumption flow.");
                    });
                }
            }
        }
    }

    private void onBillingManagerSetupFinished() {
        isIAPSetUpFinished = true;
        if (CheckNetwork.isNetworkAvailable(getApplication())) {
            CheckTopUp();
        } else {
            utility.showMessage(getString(R.string.app_name), getResources()
                    .getString(R.string.no_internet_connection), ActivityRefill.this);
        }
    }

    /**
     * Enables or disables "please wait" screen.
     */
    private void setWaitScreen(boolean set) {
        // do some blocking screen while waiting billing init
    }

    /**
     * Executes query for SKU details at the background thread
     */
    private void handleManagerAndUiReady() {
        // If Billing Manager was successfully initialized - start querying for SKUs
        setWaitScreen(true);
        querySkuDetails();
    }

    /**
     * Queries for in-app and subscriptions SKU details and updates an adapter with new data
     */
    private void querySkuDetails() {
        long startTime = System.currentTimeMillis();

        Timber.e("querySkuDetails() got subscriptions and inApp SKU details lists for: "
                + (System.currentTimeMillis() - startTime) + "ms");

        if (!isFinishing()) {

            List<String> skus = new ArrayList<>();
            for (RefillListItem item : mListItems) {
                skus.add(item.getIos_store_id());
                Timber.e("StoreItemId: " + item.getIos_store_id());
            }
            String billingType = BillingClient.SkuType.INAPP;
            getBillingManager().querySkuDetailsAsync(billingType, skus, (billingResult, skuDetailsList) -> {
                int responseCode = billingResult.getResponseCode();
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Timber.e("Unsuccessful query for type: " + billingType
                            + ". Error code: " + responseCode);
                } else if (skuDetailsList != null
                        && skuDetailsList.size() > 0) {
                    // Then fill all the other rows
                    for (SkuDetails details : skuDetailsList) {
                        Timber.e("Adding sku: " + details);
                        dataList.add(new SkuRowData(details, billingType));
                    }

                    if (dataList.size() == 0) {
                        displayAnErrorIfNeeded();
                    } else {
                        setWaitScreen(false);
                    }
                } else {
                    Timber.e("skuDetailsList is null");
                    // Handle empty state
                    displayAnErrorIfNeeded();
                }
            });
        }
    }

    private void displayAnErrorIfNeeded() {
        if (isFinishing()) {
            Timber.e("No need to show an error - activity is finishing already");
            return;
        }


//        int billingResponseCode = mBillingProvider.getBillingManager()
//                .getBillingClientResponseCode();
//
//        switch (billingResponseCode) {
//            case BillingClient.BillingResponseCode.OK:
//                // If manager was connected successfully, then show no SKUs error
//                mErrorTextView.setText(getText(R.string.error_no_skus));
//                break;
//            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
//                mErrorTextView.setText(getText(R.string.error_billing_unavailable));
//                break;
//            default:
//                mErrorTextView.setText(getText(R.string.error_billing_default));
//        }

    }


    public void verifyPurchasedWithServer(VerifyIAPRequestModel requestModel) {
        if (!DialogManager.isShowing()) {
            DialogManager.getInstance().showDialog(this, this.getResources().getString(R.string.connecting_msg));
        }

        compositeDisposable.add(
                AppsterWebServices.get().verifyIAPPurchased(AppsterApplication.mAppPreferences.getUserTokenRequest(),
                        requestModel)
                        .subscribe(verifyIAPResponeModel -> {
                            DialogManager.getInstance().dismisDialog();
                            if (verifyIAPResponeModel == null) return;
                            if (verifyIAPResponeModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                                Timber.e("Bean =" + verifyIAPResponeModel.getData().getTotalBeanIncrease());
                                AppsterApplication.mAppPreferences.getUserModel().setGems(verifyIAPResponeModel.getData().getTotalBeanIncrease());
                                for (RefillListItem item : mListItems) {
                                    if (item.getIos_store_id().equals(requestModel.getStore_id())) {
                                        // Amplitude
//                                        EventTracker.trackRevenue(productID, item.getIos_store_id());
                                    }
                                }
                                updateGem();
                                AppsterApplication.mAppPreferences.removeOrderObjectToList(requestModel);
                            }
                            shouldAllowBack = true;
                        }, error -> {
                            DialogManager.getInstance().dismisDialog();
                            Timber.e(error.getMessage());
                            shouldAllowBack = true;
                        }));

    }

}
