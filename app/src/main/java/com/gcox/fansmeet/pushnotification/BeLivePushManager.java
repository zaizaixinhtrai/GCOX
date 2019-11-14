package com.gcox.fansmeet.pushnotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.request.RequestOptions;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.ConstantBundleKey;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.customview.hashtag.FollowItem;
import com.gcox.fansmeet.features.main.MainActivity;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.StringUtil;
import com.gcox.fansmeet.util.Utils;
import com.gcox.fansmeet.util.glide.GlideApp;
import com.gcox.fansmeet.util.glide.GlideRequest;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by gaku on 12/28/17.
 */

public class BeLivePushManager {

    public static final int NOTIFICATION_BIG_PICTURE_WIDTH = Utils.dpToPx(450);
    public final static int NOTIFICATION_BIG_PICTURE_HEIGHT = Utils.dpToPx(192);
    private static final String NOTIFICATION_CHANNEL_ID = "com.appsters.CHANNEL_ID";
    private static final String NOTIFICATION_CHANNEL_NAME = "belive noti";
    protected static BeLivePushManager mInstance = new BeLivePushManager();

    public static BeLivePushManager getInstance() {
        return mInstance;
    }

    public NotificationPushModel getPushModelFromJson(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(message, NotificationPushModel.class);
        } catch (Exception e) {
            Timber.e("json format is wrong. message=" + message);
        }
        return null;
    }

    public boolean handlePushData(NotificationPushModel pushModel, Context context) {
        int notifyType = pushModel.getType();
        int unreadMessage = AppsterApplication.mAppPreferences.getNumberUnreadNotification();
        unreadMessage++;
        boolean result = true;
        Timber.e("shouldShowNotification %s", result);
        if (result) {
            switch (notifyType) {
                case Constants.NOTIFYCATION_TYPE_RECEIVE_GIFT:
                case Constants.NOTIFYCATION_TYPE_FROMADMINCREDIT:
                case Constants.NOTIFYCATION_TYPE_COMISSION:
                    break;

                case Constants.NOTIFICATION_DAILY_BONUS_NEW_DAY:
                    break;

                case Constants.NOTIFICATION_DAILY_BONUS_RESET_WEEK:
                    break;

                case Constants.NOTIFYCATION_TYPE_MESSAGE:
                    unreadMessage--;
                    break;

                case Constants.NOTIFYCATION_TYPE_LIVESTREAM:
                    if (isCurrentUserPush(pushModel.getUserId())) {
                        // ignore same user push
                        result = false;
                    }

                    unreadMessage--;
                    break;
                case Constants.NOTIFYCATION_TYPE_FOLLOW:
                    onFollowNotifyReceived(pushModel);
                    break;
                case Constants.NOTIFYCATION_TYPE_FROMADMINMSG:
                    break;
                case Constants.NOTIFYCATION_TYPE_ADMINBONUSGIFT:
                    break;
                default:
                    Timber.d("message just for showing");
                    break;
            }
        }

//        EventBus.getDefault().post(new EventBusPushNotification(unreadMessage, pushModel));

        return result;
    }

    private boolean isCurrentUserPush(int userId) {
        return AppsterApplication.mAppPreferences.getUserModel().getUserId() == userId;
    }

//    private boolean shouldShowNotification(int notifyType, Context context) {
//        String currentActivity = AppsterApplication.getCurrentActivityRunning(context);
//        return !isChatting(currentActivity, notifyType) && !isStreaming(currentActivity, notifyType) && !isNewPost(notifyType) && !isNewRecord(notifyType);
//    }
//
//    private boolean isChatting(String currentActivity, int notifyType) {
//        return (notifyType == Constants.NOTIFYCATION_TYPE_MESSAGE && currentActivity.equalsIgnoreCase(ChatActivity.class.getName()))
//                || (notifyType == Constants.NOTIFYCATION_TYPE_MESSAGE && currentActivity.equalsIgnoreCase(MessageListActivity.class.getName()));
//    }
//
//    private boolean isStreaming(String currentActivity, int notifyType) {
//        return (notifyType == Constants.NOTIFYCATION_TYPE_MESSAGE && currentActivity.equalsIgnoreCase(StreamingActivityGLPlus.class.getName()))
//                || (notifyType == Constants.NOTIFYCATION_TYPE_MESSAGE && currentActivity.equalsIgnoreCase(MediaPlayerActivity.class.getName()));
//    }

    private boolean isNewPost(int notifyType) {
        return notifyType == Constants.NOTIFYCATION_TYPE_NEWPOST;
    }

    private boolean isNewRecord(int notifyType) {
        return notifyType == Constants.NOTIFYCATION_TYPE_NEW_RECORD;
    }

    public int getNotifyType(NotificationPushModel notificationEntity) {
        String notifyTypeString = String.valueOf(notificationEntity.postType) + String.valueOf(notificationEntity.getUserId());
        int notifyType;
        try {
            notifyType = Integer.parseInt(notifyTypeString);
        } catch (Exception e) {
            Timber.e(e);
            notifyType = 0;
        }
        return notifyType;
    }

    public NotificationCompat.Builder createNotificationBuilder(NotificationPushModel notificationEntity, NotificationCompat.Builder outBuilder, Context context) {
        createChannel(context);
        int notifyType = getNotifyType(notificationEntity);

        int size = Utils.dpToPx(48);
        Bitmap bigIcon = fetchImageSync(notificationEntity.getUserImage(), size, size, true, context);

        //setup notification
        String title = TextUtils.isEmpty(notificationEntity.getTitle()) ? context.getString(R.string.app_name) : notificationEntity.getTitle();
        String message = '\u200e' + notificationEntity.getMessage() + '\u200e';
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ConstantBundleKey.BUNDLE_NOTIFICATION_KEY, notificationEntity);
        if (!isDailyBonusType(notifyType)) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyType /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Set no action on Suggestion screen
//        if (!AppsterApplication.getCurrentActivityRunning(context).equals(FriendSuggestionActivity.class.getName())) {
//            pendingIntent = PendingIntent.getActivity(context, notifyType /* Request code */, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//        }

        NotificationCompat.Style style;
        if (TextUtils.isEmpty(notificationEntity.getPushImageUrl())) {
            style = new NotificationCompat.BigTextStyle().bigText(message);

        } else {// has a picture
            Bitmap bigPicture = fetchImageSync(notificationEntity.getPushImageUrl(), 0, 0, false, context);
            bigPicture = centerInsidePicture(bigPicture);
            style = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPicture)
                    .setSummaryText(message);
        }

        NotificationCompat.Builder notificationBuilder;
        if (outBuilder != null) {
            notificationBuilder = outBuilder;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            } else {
                notificationBuilder = new NotificationCompat.Builder(context);
            }
        }

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.accent));
//        }
        notificationBuilder.setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(StringUtil.decodeString(message))
                .setStyle(style)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (bigIcon != null) {
            notificationBuilder.setLargeIcon(bigIcon);
        }

        notificationBuilder.setVibrate(new long[]{500, 500});
        UserModel userModel = AppsterApplication.mAppPreferences.getUserModel();
        if (userModel != null && userModel.getNotificationSound() == 1) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(defaultSoundUri);
            OneSignal.enableSound(true);

        } else {
            OneSignal.enableSound(false);
        }

        return notificationBuilder;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    public void pushNotification(NotificationPushModel notificationEntity, Context context) {

        int notifyType = getNotifyType(notificationEntity);
        NotificationCompat.Builder notificationBuilder = createNotificationBuilder(notificationEntity, null, context);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyType /* ID of notification */, notification);
    }

    public boolean isDailyBonusType(int notifyType) {
        return notifyType == 29 || notifyType == 30;
    }


    /**
     * Use Picasso to load image from remote server synchronously
     *
     * @return the user avatar if success otherwise the launcher icon will be returned
     */
    @SuppressWarnings("CheckResult")
    private Bitmap fetchImageSync(String url, int width, int height, boolean isCircleTransform, Context context) {
        Bitmap bitmap = null;

        if (url != null && !url.isEmpty()) {

            try {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.error(R.mipmap.ic_launcher);

                if (width > 0 && height > 0) {
                    requestOptions.override(width, height);
                }

                GlideRequest<Bitmap> glideReq = GlideApp.with(context)
                        .asBitmap()
                        .apply(requestOptions)
                        .load(url);

                if (isCircleTransform) {
                    //requestOptions.transform(new CircleTransformation(0));
                    glideReq.circleCrop();
                }

                bitmap = glideReq.submit().get();

            } catch (InterruptedException | ExecutionException e) {
                Timber.e(e, "url=" + url);
            }
        }

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        return bitmap;
    }

    private Bitmap centerInsidePicture(Bitmap srcBitmap) {
        if (srcBitmap == null) {
            return null;
        }
        Bitmap canvasBmp;

        // Calculate the number of pixels of width and height of the target with the pixel density taken into account
        int targetWidthPx = NOTIFICATION_BIG_PICTURE_WIDTH;
        int targetHeightPx = NOTIFICATION_BIG_PICTURE_HEIGHT;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        // padding to get Matrix is ​​0 and small images do not enlarge
        Matrix mat = getMatrix(srcWidth, srcHeight, targetWidthPx, targetHeightPx, 0, false);

        // Create a Paint
        Paint bmpPaint = new Paint();
        bmpPaint.setFilterBitmap(true);

        // Create a canvas whose background is transparent with target width and height
        canvasBmp = Bitmap.createBitmap(targetWidthPx, targetHeightPx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBmp);

        // Composite images on a transparent canvas
        canvas.drawBitmap(srcBitmap, mat, bmpPaint);

        Timber.i("srcWidth ="
                + srcWidth + "srcHeight =" + srcHeight + "targetWidthPx ="
                + targetWidthPx + "targetHeightPx =" + targetHeightPx);
        return canvasBmp;
    }

    /**
     * @return Scale value to fit
     * @ Param sw Source width
     * @ Param sh Source height
     * @ Param pw Fit this width
     * @ Param ph Fit this height
     * @ Param padding Padding to consider when fitting
     * @ Param enableMagnify Whether or not to enlarge when the image is small
     */
    private static Matrix getMatrix(int sw, int sh, int pw, int ph, int padding, boolean enableMagnify) {
        Float scale = getMaxScaleToParent(sw, sh, pw, ph, padding);
        if (!enableMagnify && scale > 1.0f) {
            scale = 1f;
        }
        Timber.i("# getMatrix ()", "scale =" + scale);
        Matrix mat = new Matrix();
        mat.postScale(scale, scale);
        mat.postTranslate((pw - (int) (sw * scale)) / 2, (ph - (int) (sh * scale)) / 2);
        return mat;
    }

    private static float getMaxScaleToParent(int sw, int sh, int pw, int ph, int padding) {
        Float hScale = (float) (pw - padding) / (float) sw;
        Float vScale = (float) (ph - padding) / (float) sh;
        return Math.min(hScale, vScale);
    }

    /**
     * check whether the notification is belong to current user or not,
     * if not then should ignore this notification
     *
     * @param userId of user should received the notification sent from server
     * @return true if the current user id and user id `sent by server are not equal.
     */
    private boolean shouldIgnoreNotification(int userId) {
        return AppsterApplication.mAppPreferences.getUserModel().getUserId() != userId;
    }

    private void onFollowNotifyReceived(NotificationPushModel pushModel) {
        Timber.d("onFollowNotifyReceived thread %s", Thread.currentThread().getName());
        FollowItem followItem = new FollowItem();
        followItem.setUserId(String.valueOf(pushModel.getUserId()));
        followItem.setDisplayName(pushModel.getUserDisplayName());
        followItem.setUserName(pushModel.getUserName());
        followItem.setProfilePic(pushModel.getUserName());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(followItem);
        realm.commitTransaction();
        realm.close();
    }

    @SuppressLint("NewApi")
    private void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
