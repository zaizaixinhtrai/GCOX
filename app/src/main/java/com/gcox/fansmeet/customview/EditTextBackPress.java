package com.gcox.fansmeet.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by User on 4/4/2016.
 */

public class EditTextBackPress extends EditText {

    Context context;
    private static Activity mActivity;

    public static void sethActivity(Activity searchActivity) {
        mActivity = searchActivity;
    }

    public EditTextBackPress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard

            if (mActivity != null && !mActivity.isFinishing()) {
                mActivity.onBackPressed();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}