package com.gcox.fansmeet.pushnotification;

import android.content.Context;
import com.gcox.fansmeet.models.UserModel;
import com.onesignal.OneSignal;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;


/**
 * Created by gaku on 12/22/17.
 */

public class OneSignalUtil {

    public static String TAG_USER_NAME = "user_name";
    public static String TAG_SYSTEM_NOTIFICATION = "pn_system";
    public static String TAG_LIVE_NOTIFICATION = "pn_live";

    public static void init(Context context) {
        OneSignal.startInit(context)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new OneSignalNotificationService.PushOpenedHandler(context))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public static void setUser(UserModel user) {
        if (user == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject();
            json.put(TAG_USER_NAME, user.getUserName());
//            json.put(TAG_SYSTEM_NOTIFICATION, user.getNotification());
//            json.put(TAG_LIVE_NOTIFICATION, user.getLiveNotification());
            OneSignal.sendTags(json);

        } catch (JSONException e) {
            Timber.e(e);
        }
    }

}
