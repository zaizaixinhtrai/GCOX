package com.gcox.fansmeet.util;

import android.view.View;

/**
 * Created by linh on 30/11/2016.
 */

public abstract class DoubleTabListener implements View.OnClickListener {
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleTap(v);
        } else {
            onSingleTap(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleTap(View v);
    public abstract void onDoubleTap(View v);
}
