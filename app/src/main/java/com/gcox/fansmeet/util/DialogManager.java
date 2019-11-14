package com.gcox.fansmeet.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.dialog.ProgressHUD;
import timber.log.Timber;

/**
 * Created by User on 9/8/2015.
 */
public class DialogManager {

    private DialogManager() {
    }

    private static DialogManager instance;
    private ProgressHUD dialog;

    private static boolean isShowing = false;

    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
        }
        return instance;
    }


    public static boolean isShowing() {
        return isShowing;
    }

    private static synchronized void setIsShowing(boolean showing) {
        isShowing = showing;
    }

    public void showDialog(Context context, String message) {
        if(context instanceof Activity) {
            if(!((Activity) context).isFinishing()) {
                showDialog(context, message, false);
            }
        }
    }

    public void showDialog(Context context, String message, boolean fullscreen) {

        dismisDialog();

        dialog = new ProgressHUD(context,
                R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hudd);
//        if (message == null || message.length() == 0) {
//            dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
//        } else {
//            TextView txt = (TextView) dialog.findViewById(R.id.message);
//            txt.setText(message);
//        }
        dialog.setCancelable(false);
        final Window window = dialog.getWindow();
        if (window != null) {
            window.getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.2f;
            window.setAttributes(lp);

            if (fullscreen) {
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                immersiveMode(window);
            }

        }
        dialog.show();
        if (window != null && fullscreen)
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        setIsShowing(true);

    }

    private void immersiveMode(Window window) {
        final View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void dismisDialog() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (Exception e) {
                Timber.d(e);
            }

            setIsShowing(false);
        }
    }
}
