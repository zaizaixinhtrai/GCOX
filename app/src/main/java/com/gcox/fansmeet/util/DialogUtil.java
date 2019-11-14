package com.gcox.fansmeet.util;

import android.app.Activity;
import com.gcox.fansmeet.R;


/**
 * Created by linh on 10/02/2017.
 */

public class DialogUtil {

    public static void showConfirmUnFollowUser(Activity activity, String displayName, DialogbeLiveConfirmation.OnBeLiveDialogClickedListener okCallback){
        showConfirmUnFollowUser(activity, displayName, okCallback, null);
    }

    public static void showConfirmUnFollowUser(Activity activity, String displayName, DialogbeLiveConfirmation.OnBeLiveDialogClickedListener okCallback, DialogbeLiveConfirmation.OnCancelBeLiveDialogClickedListener cancelCallback){
        String message = activity.getString(R.string.unfollow_confirmation) + " " + displayName;
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(activity.getString(R.string.app_name))
                .message(message)
                .confirmText(activity.getString(R.string.btn_text_ok))
                .singleAction(false)
                .onConfirmClicked(okCallback)
                .onCancelClicked(cancelCallback)
                .build().show(activity);
    }

    public static void showConfirmIgnoreLiveStream(Activity activity, DialogbeLiveConfirmation.OnBeLiveDialogClickedListener okCallback, DialogbeLiveConfirmation.OnCancelBeLiveDialogClickedListener cancelCallback){
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(activity.getString(R.string.delete_confirm_title))
                .message(activity.getString(R.string.delete_confirm_message))
                .confirmText(activity.getString(R.string.btn_text_ok))
                .singleAction(false)
                .confirmText(activity.getString(R.string.delete))
                .onConfirmClicked(okCallback)
                .onCancelClicked(cancelCallback)
                .build().show(activity);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String buttonName,
                                         DialogbeLiveConfirmation.OnBeLiveDialogClickedListener onOkListener) {
        showConfirmDialog(activity, title, message, buttonName, onOkListener, null);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String buttonName,
                                         DialogbeLiveConfirmation.OnBeLiveDialogClickedListener onOkListener,
                                         DialogbeLiveConfirmation.OnCancelBeLiveDialogClickedListener onCancelListener) {
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(title)
                .message(message)
                .confirmText(buttonName)
                .onConfirmClicked(onOkListener)
                .onCancelClicked(onCancelListener)
                .build().show(activity);
    }

    public static void showConfirmDialogSingleAction(Activity activity, String title, String message, String buttonName,
                                                     DialogbeLiveConfirmation.OnBeLiveDialogClickedListener onOkListener){
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(title)
                .message(message)
                .confirmText(buttonName)
                .onConfirmClicked(onOkListener)
                .singleAction(true)
                .build().show(activity);
    }

}
