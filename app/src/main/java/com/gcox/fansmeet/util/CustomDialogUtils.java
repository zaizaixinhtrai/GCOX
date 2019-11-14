package com.gcox.fansmeet.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gcox.fansmeet.R;
import timber.log.Timber;

/**
 * Created by sonnguyen on 10/5/15.
 */
public class CustomDialogUtils {

    public static void openRecordVideoDialog(@NonNull Context context,
                                             @NonNull String title,
                                             final View.OnClickListener clickOnRecordVideo,
                                             final View.OnClickListener clickOnChooseVideo) {
        if (context != null && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_take_photo);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            if (!TextUtils.isEmpty(title)) {
                TextView tvTitle = (TextView) dialog.findViewById(R.id.Sharevia);
                tvTitle.setText(title);
            }

            final Button btnRecordVideo = (Button) dialog.findViewById(R.id.btn_take_photo);
            btnRecordVideo.setOnClickListener(v -> {
                if (clickOnRecordVideo != null) {
                    clickOnRecordVideo.onClick(v);
                }
                dialog.cancel();
            });

            final Button btnChooseVideo = (Button) dialog.findViewById(R.id.btn_choose_photo);
            btnChooseVideo.setOnClickListener(v -> {
                if (clickOnChooseVideo != null) {
                    clickOnChooseVideo.onClick(v);
                }
                dialog.cancel();
            });

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(v -> dialog.cancel());

            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(v -> dialog.dismiss());


            dialog.show();
        }

    }

    public static void showPopUpFollowUnFollow(Context context, final View.OnClickListener clickFollowUnFollow) {
        if (context != null && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_follow_unfollow);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Button btnClick = (Button) dialog.findViewById(R.id.btn_follow);

            btnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFollowUnFollow.onClick(v);
                    dialog.dismiss();
                }
            });
            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    public static void showUserOptionPopUp(Context context, ReportAndBlockCallback callback) {
        if (context != null && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_follow_unfollow);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Button btnReport = (Button) dialog.findViewById(R.id.btn_follow);
            Button btnBlock = (Button) dialog.findViewById(R.id.btn_block);
            btnReport.setText(context.getString(R.string.report_user));
            btnReport.setOnClickListener(v -> {
                callback.onReportClick();
                dialog.dismiss();
            });
            btnBlock.setOnClickListener(view -> {
                callback.onBlockClick();
                dialog.dismiss();

            });


            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(v -> dialog.dismiss());

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    public static void showOwnerFeedOptionPopup(Context context, FeedOptionCallback callback, String option0, String option1) {
        if (context != null && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_feed_owner_option);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Button btnOption0 = (Button) dialog.findViewById(R.id.btn_option0);
            Button btnOption1 = (Button) dialog.findViewById(R.id.btn_option1);
            if (option0 != null) btnOption0.setText(option0);
            if (option1 != null) btnOption1.setText(option1);
            btnOption0.setOnClickListener(view -> {
                callback.onOptionClicked(0);
                dialog.dismiss();
            });
            btnOption1.setOnClickListener(view -> {
                callback.onOptionClicked(1);
                dialog.dismiss();
            });

            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(v -> dialog.dismiss());

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    public static void showFeedOptionPopup(Context context, FeedOptionCallback callback, String option0, String option1, String option2) {
        if (context != null && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_feed_option);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Button btnOption0 = (Button) dialog.findViewById(R.id.btn_option0);
            Button btnOption1 = (Button) dialog.findViewById(R.id.btn_option1);
            Button btnOption2 = (Button) dialog.findViewById(R.id.btn_option2);
            if (option0 != null) btnOption0.setText(option1);
            if (option1 != null) btnOption1.setText(option2);
            if (option2 != null) btnOption2.setText(option0);

            btnOption0.setOnClickListener(view -> {
                callback.onOptionClicked(1);
                dialog.dismiss();
            });
            btnOption1.setOnClickListener(view -> {
                callback.onOptionClicked(2);
                dialog.dismiss();
            });
            btnOption2.setOnClickListener(view -> {
                callback.onOptionClicked(0);
                dialog.dismiss();
            });

            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(v -> dialog.dismiss());

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    public static void showFeedOptionPopup(Activity context, FeedOptionCallback callback, String option0, String option1, String option2) {
        if (context != null && !context.isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.Theme_DialogCustom);
            dialog.setContentView(R.layout.dialog_feed_option);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            Button btnOption0 = (Button) dialog.findViewById(R.id.btn_option0);
            Button btnOption1 = (Button) dialog.findViewById(R.id.btn_option1);
            Button btnOption2 = (Button) dialog.findViewById(R.id.btn_option2);
            if (option0 != null) btnOption0.setText(option1);
            if (option1 != null) btnOption1.setText(option2);
            if (option2 != null) btnOption2.setText(option0);

            btnOption0.setOnClickListener(view -> {
                callback.onOptionClicked(1);
                dialog.dismiss();
            });
            btnOption1.setOnClickListener(view -> {
                callback.onOptionClicked(2);
                dialog.dismiss();
            });
            btnOption2.setOnClickListener(view -> {
                callback.onOptionClicked(0);
                dialog.dismiss();
            });

            RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativelayout_photo_dialog_main);
            rlMain.setOnClickListener(v -> dialog.dismiss());

            Button btnCancel = (Button) dialog.findViewById(R.id.button_cancel_photo);
            btnCancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

//    private String getDeleteMessage(Context mActivity,boolean streamItem) {
//        return streamItem ? mActivity.getString(R.string.newsfeed_menu_del_stream) : mActivity.getString(R.string.newsfeed_menu_del_post);
//    }
//
//    private String getReportMessage(Context mActivity,int isReport) {
//        return isReport == Constants.HAS_BEEN_REPORT_POST ? mActivity.getString(R.string.newsfeed_menu_unrepost) : mActivity.getString(R.string.newsfeed_menu_repost);
//    }
//
//    private String getFollowMessage(int isFollow) {
//        return isFollow == Constants.IS_FOLLOWING_USER ? mActivity.getString(R.string.newsfeed_menu_unfolow) : mActivity.getString(R.string.newsfeed_menu_folow);
//    }

    public interface ReportAndBlockCallback {
        void onReportClick();

        void onBlockClick();

        void onBanClick();
    }

    public interface FeedOptionCallback {
        void onOptionClicked(int position);
    }
}
