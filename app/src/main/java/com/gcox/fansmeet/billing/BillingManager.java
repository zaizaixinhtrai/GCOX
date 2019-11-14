package com.gcox.fansmeet.billing;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.BuildConfig;

import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.features.refill.RefillListItem;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel;
import io.reactivex.disposables.CompositeDisposable;
import org.json.JSONException;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

/**
 * Handles all the interactions with Play Store (via Billing library), maintains connection to
 * it through BillingClient and caches temporary states/data if needed
 */
public class BillingManager implements PurchasesUpdatedListener {
    // Default value of mBillingClientResponseCode until BillingManager was not yeat initialized
    public static final int BILLING_MANAGER_NOT_INITIALIZED = -1;

    private static final String TAG = "BillingManager";

    /**
     * A reference to BillingClient
     **/
    private BillingClient mBillingClient;

    /**
     * True if billing service is connected now.
     */
    private boolean mIsServiceConnected;

    private final BillingUpdatesListener mBillingUpdatesListener;

    private final Activity mActivity;

    private final List<Purchase> mPurchases = new ArrayList<>();

    private Set<String> mTokensToBeConsumed;

    private int mBillingClientResponseCode = BILLING_MANAGER_NOT_INITIALIZED;

    /* BASE_64_ENCODED_PUBLIC_KEY should be YOUR APPLICATION'S PUBLIC KEY
     * (that you got from the Google Play developer console). This is not your
     * developer public key, it's the *app-specific* public key.
     *
     * Instead of just storing the entire literal string here embedded in the
     * program,  construct the key at runtime from pieces or
     * use bit manipulation (for example, XOR with some other string) to hide
     * the actual key.  The key itself is not secret information, but we don't
     * want to make it easy for an attacker to replace the public key with one
     * of their own and then fake messages from the server.
     */
    private static final String BASE_64_ENCODED_PUBLIC_KEY = BuildConfig.IAP_KEY64BIT;

    /**
     * Listener to the updates that happen when purchases list was updated or consumption of the
     * item was finished
     */
    public interface BillingUpdatesListener {
        void onBillingClientSetupFinished();

        void onPurchasesUpdated(List<Purchase> purchases);
    }

    /**
     * Listener for the Billing client state to become connected
     */
    public interface ServiceConnectedListener {
        void onServiceConnected(@BillingClient.BillingResponseCode int resultCode);
    }

    public BillingManager(Activity activity, final BillingUpdatesListener updatesListener) {
        Log.d(TAG, "Creating Billing client.");
        mActivity = activity;
        mBillingUpdatesListener = updatesListener;
        mBillingClient = BillingClient.newBuilder(mActivity).setListener(this)
                .enablePendingPurchases() // required or app will crash
                .build();

        Log.d(TAG, "Starting setup.");
        connectToPlayBillingService();

    }

    private void connectToPlayBillingService() {
        // Start setup. This is asynchronous and the specified listener will be called
        // once setup completes.
        // It also starts to report all the new purchases through onPurchasesUpdated() callback.
        startServiceConnection(() -> {
            // Notifying the listener that billing client is ready
            mBillingUpdatesListener.onBillingClientSetupFinished();
            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Timber.e(TAG, "Setup successful. Querying inventory.");
            queryPurchases();
        });
    }

    /**
     * Handle a callback that purchases were updated from the Billing library
     */
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        int resultCode = billingResult.getResponseCode();
        if (resultCode == BillingClient.BillingResponseCode.OK) {
//            for (Purchase purchase : purchases) {
//                handlePurchase(purchase);
//            }
            mBillingUpdatesListener.onPurchasesUpdated(purchases);
        } else if (resultCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping");
        } else if (resultCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            // item already owned? call queryPurchases to verify and process all such items
            Log.d(TAG, billingResult.getDebugMessage());
            queryPurchases();
        } else if (resultCode == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
            connectToPlayBillingService();
        } else {
            Log.w(TAG, "onPurchasesUpdated() got unknown resultCode: " + resultCode);
        }
    }


    /**
     * Start a purchase flow
     */
    public void initiatePurchaseFlow(final String json) {
        try {
            initiatePurchaseFlow(new SkuDetails(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a purchase or subscription replace flow
     */
    public void initiatePurchaseFlow(final SkuDetails skuDetails) {
        Runnable purchaseFlowRequest = () -> {
//                String oldSku = getOldSku(skuDetails.getSku());
//                Log.d(TAG, "Launching in-app purchase flow. Replace old SKU? " + (oldSku != null));
            BillingFlowParams purchaseParams = null;
            try {
                purchaseParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(new SkuDetails(skuDetails.getOriginalJson())).build();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mBillingClient.launchBillingFlow(mActivity, purchaseParams);
        };

        executeServiceRequest(purchaseFlowRequest);
    }

    public Context getContext() {
        return mActivity;
    }

    /**
     * Clear the resources
     */
    public void destroy() {
        Log.d(TAG, "Destroying the manager.");

        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList,
                                     final SkuDetailsResponseListener listener) {
        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable queryRequest = () -> {
            // Query the purchase async
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(itemType);
            mBillingClient.querySkuDetailsAsync(params.build(),
                    (billingResult, skuDetailsList) -> listener.onSkuDetailsResponse(billingResult, skuDetailsList));
        };

        executeServiceRequest(queryRequest);
    }

    public void consumeAsync(final String purchaseToken, final ConsumeResponseListener onConsumeListener) {
        // If we've already scheduled to consume this token - no action is needed (this could happen
        // if you received the token when querying purchases inside onReceive() and later from
        // onActivityResult()
//        if (mTokensToBeConsumed == null) {
//            mTokensToBeConsumed = new HashSet<>();
//        } else if (mTokensToBeConsumed.contains(purchaseToken)) {
//            Log.i(TAG, "Token was already scheduled to be consumed - skipping...");
//            return;
//        }
//        mTokensToBeConsumed.add(purchaseToken);

//        // Generating Consume Response listener
//        final ConsumeResponseListener onConsumeListener = (billingResult, purchaseToken1) -> {
//            // If billing service was disconnected, we try to reconnect 1 time
//            // (feel free to introduce your retry policy here).
//            mBillingUpdatesListener.onConsumeFinished(purchaseToken1, billingResult.getResponseCode());
//
//        };

        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable consumeRequest = () -> {
            // Consume the purchase async
            ConsumeParams params = ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build();
            mBillingClient.consumeAsync(params, onConsumeListener);
        };

        executeServiceRequest(consumeRequest);
    }

    /**
     * Returns the value Billing client response code or BILLING_MANAGER_NOT_INITIALIZED if the
     * clien connection response was not received yet.
     */
    public int getBillingClientResponseCode() {
        return mBillingClientResponseCode;
    }

    /**
     * Handles the purchase
     * <p>Note: Notice that for each purchase, we check if signature is valid on the client.
     * It's recommended to move this check into your backend.
     * </p>
     *
     * @param purchase Purchase to be handled
     */
    private void handlePurchase(Purchase purchase) {
        Log.d(TAG, "Got a verified purchase: " + purchase);

        mPurchases.add(purchase);
    }

    /**
     * Handle a result from querying of purchases and report an updated list to the listener
     */
    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (mBillingClient == null || result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "Billing client was null or result code (" + result.getResponseCode()
                    + ") was bad - quitting");
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        // Update the UI and purchases inventory with new list of purchases
        mPurchases.clear();
        onPurchasesUpdated(result.getBillingResult(), result.getPurchasesList());

        // check transaction purchase in cache
        checkTransactionPurchase();
    }

    private void checkTransactionPurchase() {
        ArrayList<VerifyIAPRequestModel> orderIds = AppsterApplication.mAppPreferences.loadOrderObjectToList();
        if (orderIds != null) {
            for (int i = 0; i < orderIds.size(); i++) {
                verifyPurchasedWithServer(orderIds.get(i));
            }
        }
    }

    private void verifyPurchasedWithServer(VerifyIAPRequestModel requestModel) {

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                AppsterWebServices.get().verifyIAPPurchased(AppsterApplication.mAppPreferences.getUserTokenRequest(),
                        requestModel)
                        .subscribe(verifyIAPResponeModel -> {
                            DialogManager.getInstance().dismisDialog();
                            if (verifyIAPResponeModel == null) return;
                            if (verifyIAPResponeModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                                AppsterApplication.mAppPreferences.getUserModel().setGems(verifyIAPResponeModel.getData().getTotalBeanIncrease());
                                AppsterApplication.mAppPreferences.removeOrderObjectToList(requestModel);
                            }
                            compositeDisposable.dispose();
                        }, error -> {
                            DialogManager.getInstance().dismisDialog();
                            Timber.e(error.getMessage());
                            compositeDisposable.dispose();
                        }));

    }

    /**
     * Checks if subscriptions are supported for current client
     * <p>Note: This method does not automatically retry for RESULT_SERVICE_DISCONNECTED.
     * It is only used in unit tests and after queryPurchases execution, which already has
     * a retry-mechanism implemented.
     * </p>
     */
    public boolean areSubscriptionsSupported() {
        int responseCode = mBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS).getResponseCode();
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "areSubscriptionsSupported() got an error response: " + responseCode);
        }
        return responseCode == BillingClient.BillingResponseCode.OK;
    }

    /**
     * Query purchases across various use cases and deliver the result in a formalized way through
     * a listener
     */
    public void queryPurchases() {
        Runnable queryToExecute = () -> {
            long time = System.currentTimeMillis();
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            Log.i(TAG, "Querying purchases elapsed time: " + (System.currentTimeMillis() - time)
                    + "ms");
//                // If there are subscriptions supported, we add subscription rows as well
            if (areSubscriptionsSupported()) {
                Purchase.PurchasesResult subscriptionResult
                        = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
//                    Log.i(TAG, "Querying purchases and subscriptions elapsed time: "
//                            + (System.currentTimeMillis() - time) + "ms");
//                    Log.i(TAG, "Querying subscriptions result code: "
//                            + subscriptionResult.getResponseCode()
//                            + " res: " + subscriptionResult.getPurchasesList().size());

                if (subscriptionResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    purchasesResult.getPurchasesList().addAll(
                            subscriptionResult.getPurchasesList());
                } else {
                    Log.e(TAG, "Got an error response trying to query subscription purchases");
                }
            } else if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.i(TAG, "Skipped subscription purchases query since they are not supported");
            } else {
                Log.w(TAG, "queryPurchases() got an error response code: "
                        + purchasesResult.getResponseCode());
            }
            onQueryPurchasesFinished(purchasesResult);
        };

        executeServiceRequest(queryToExecute);
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                int billingResponseCode = billingResult.getResponseCode();
                Timber.e(TAG, "Setup finished. Response code: " + billingResponseCode);

                if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
                mBillingClientResponseCode = billingResponseCode;
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    private void handleConsumablePurchasesAsync(List<Purchase> consumables) {
        Log.d(TAG, "handleConsumablePurchasesAsync called");
        for (Purchase purchase : consumables) {
            Timber.d("handleConsumablePurchasesAsync foreach it is %s", purchase);
            ConsumeParams params = ConsumeParams.newBuilder().setPurchaseToken(purchase
                    .getPurchaseToken()).build();
            mBillingClient.consumeAsync(params, null);

        }
    }

    /**
     * If you do not acknowledge a purchase, the Google Play Store will provide a refund to the
     * users within a few days of the transaction. Therefore you have to implement
     * [BillingClient.acknowledgePurchaseAsync] inside your app.
     */
    private void acknowledgeNonConsumablePurchasesAsync(List<Purchase> nonConsumables) {
        for (Purchase purchase : nonConsumables) {
            AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase
                    .getPurchaseToken()).build();
            mBillingClient.acknowledgePurchase(params, null);

        }
    }

    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            Timber.e("executeServiceRequest");
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }

}