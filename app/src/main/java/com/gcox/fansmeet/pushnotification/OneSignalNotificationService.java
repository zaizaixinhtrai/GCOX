package com.gcox.fansmeet.pushnotification;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.gcox.fansmeet.common.ConstantBundleKey;
import com.gcox.fansmeet.features.main.MainActivity;
import com.onesignal.*;
import timber.log.Timber;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by gaku on 12/27/17.
 */

public class OneSignalNotificationService extends NotificationExtenderService {

    private static final String TAG = OneSignalNotificationService.class.getSimpleName();

    ConcurrentLinkedQueue<OSNotificationReceivedResult> notificationQueue = new ConcurrentLinkedQueue<OSNotificationReceivedResult>();

    @SuppressLint("BinaryOperationInTimber")
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {

        Timber.d("Received OneSignal notification. " +
                "title=" + notification.payload.title +
                ", body=" + notification.payload.body +
                ", additionalData=" + notification.payload.additionalData
        );

        BeLivePushManager pushManager = BeLivePushManager.getInstance();

        NotificationPushModel pushModel = null;
        try {
            String json = notification.payload.additionalData.getString("info");
            pushModel = pushManager.getPushModelFromJson(json);
            if (pushModel == null) {
                throw new NullPointerException("pushModel is null");
            }
        } catch (Exception e) {
            Timber.e(e,"Failed to convert to NotificationPushModel. ");
            return true;
        }

        if (!BeLivePushManager.getInstance().handlePushData(pushModel, this)) {
            // don't show push notification
            return true;
        }

        Service self = this;
        NotificationPushModel notificationEntity = pushModel;

        OverrideSettings settings = new OverrideSettings();
        settings.extender = builder -> BeLivePushManager.getInstance().createNotificationBuilder(notificationEntity, builder, self);

        OSNotificationDisplayedResult displayedResult = displayNotification(settings);
        Timber.d("Display OneSignal Notification");

        // Return true to stop the notification from displaying
        return true;
    }

    @SuppressLint("BinaryOperationInTimber")
    public static class PushOpenedHandler implements OneSignal.NotificationOpenedHandler {
        Context mContext;

        public PushOpenedHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            try {
                Timber.d("clicked notification. "
                    + "title=" + result.notification.payload.title
                    + ", body=" + result.notification.payload.body
                    + ", additionalData=" + result.notification.payload.additionalData
                );

//                String message = result.notification.payload.additionalData.getString("info");
//                Gson gson = new Gson();
//                NotificationPushModel pushModel = gson.fromJson(message, NotificationPushModel.class);

                BeLivePushManager pushManager = BeLivePushManager.getInstance();
                NotificationPushModel pushModel = null;
                try {
                    String json = result.notification.payload.additionalData.getString("info");
                    pushModel = pushManager.getInstance().getPushModelFromJson(json);
                    if (pushModel == null) {
                        throw new NullPointerException("pushModel is null");
                    }
                } catch (Exception e) {
                    Timber.e(e,"Failed to convert to NotificationPushModel. ");
                    return;
                }

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(ConstantBundleKey.BUNDLE_NOTIFICATION_KEY, pushModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                int notifyType = pushManager.getNotifyType(pushModel);
                if(!pushManager.isDailyBonusType(notifyType)) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                mContext.startActivity(intent);

            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

}
