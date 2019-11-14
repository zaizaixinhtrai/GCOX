package com.gcox.fansmeet.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.customview.hashtag.FollowItem;
import com.gcox.fansmeet.util.AppsterUtility;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.response.BaseResponse;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import timber.log.Timber;

import java.util.Date;

/**
 * Created by linh on 21/06/2017.
 */

public class RefreshFollowerListService extends IntentService {
    private static final String USER_ID = "USER_ID";
    public static final String LAST_TIME_SYNC_FOLLOWER_LIST = "LAST_TIME_SYNC_FOLLOWER_LIST";
    private static final long ONE_DAY = 86400000;//one day
    private static final long ONE_MINUTE = 60000;// one minute

    private int mUserId;
    CompositeDisposable mCompositeSubscription;

    public static Intent createIntent(Context context, String userId) {
        Intent intent = new Intent(context, RefreshFollowerListService.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RefreshFollowerListService() {
        super("RefreshFollowerListService");
        Timber.d("RefreshFollowerListService constructor");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.e("onHandleIntent");
        if (intent == null) {
            return;
        }

        Timber.e("onHandleIntent %s", mUserId);
        if (!AppsterApplication.mAppPreferences.isUserLogin()) {
            return;
        }
        mUserId = AppsterApplication.mAppPreferences.getUserModel().getUserId();
        Timber.e("onHandleIntent %s", mUserId);
        mCompositeSubscription = new CompositeDisposable();

        refreshFollowerList(mUserId, Constants.WEB_SERVICE_START_PAGE);

    }

    private void refreshFollowerList(int profileId, final int offset) {
        mCompositeSubscription.add(AppsterWebServices.get().getFollowersTagList(AppsterUtility.getAuth(), profileId,
                offset, Constants.PAGE_LIMITED_1000)
                .observeOn(Schedulers.newThread())
                .map(BaseResponse::getData)
                .subscribe(followResponseModel -> {
                    Timber.e("refreshFollowerList response ok");
                    Realm realm = Realm.getDefaultInstance();
                    if (offset == Constants.WEB_SERVICE_START_PAGE) {
                        Timber.e("deleteAllFollowerInRealm");
                        deleteAllFollowerInRealm(realm);
                    }
                    realm.executeTransaction(r -> r.copyToRealmOrUpdate(followResponseModel.getResult()));
                    if (!followResponseModel.isEnd()) {
                        int offset1 = followResponseModel.getNextId();
                        refreshFollowerList(profileId, offset1);
                    } else {
                        saveLastTimeSync(System.currentTimeMillis());
                    }
                    realm.close();
                }, this::handleError));

    }


    private void handleError(Throwable e) {
        Timber.e(e);
    }

    private void deleteAllFollowerInRealm(Realm realm) {
        realm.executeTransaction(r -> r.delete(FollowItem.class));
        saveLastTimeSync(Integer.MIN_VALUE);
        SharedPreferences sharedPreferences = AppsterApplication.mAppPreferences.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last_time_delete", new Date(System.currentTimeMillis()).toString());
        editor.apply();
    }

    @Override
    public void onDestroy() {
//        RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
        super.onDestroy();
    }

    public static boolean shouldRefreshFollowerList() {
        SharedPreferences sharedPreferences = AppsterApplication.mAppPreferences.getSharedPreferences();
        long lastTime = sharedPreferences.getLong(LAST_TIME_SYNC_FOLLOWER_LIST, Integer.MIN_VALUE);
        if (lastTime == Integer.MIN_VALUE) return true;
        long currentTime = System.currentTimeMillis();
        return currentTime - lastTime >= ONE_DAY;
    }

    private void saveLastTimeSync(long currentTime) {
        SharedPreferences sharedPreferences = AppsterApplication.mAppPreferences.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_TIME_SYNC_FOLLOWER_LIST, currentTime);
        editor.apply();
    }

    public static void clearLastTimeSync() {
        SharedPreferences sharedPreferences = AppsterApplication.mAppPreferences.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_TIME_SYNC_FOLLOWER_LIST, Integer.MIN_VALUE);
        editor.apply();
    }
}
