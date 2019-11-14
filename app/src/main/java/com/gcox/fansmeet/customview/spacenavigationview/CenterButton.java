package com.gcox.fansmeet.customview.spacenavigationview;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;

/**
 * Created by Chatikyan on 10.11.2016.
 */

public class CenterButton extends FloatingActionButton {

    public CenterButton(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if (!result) {
            if(ev.getAction() == MotionEvent.ACTION_UP) {
                cancelLongPress();
            }
            setPressed(false);
        }
        return result;
    }
}
