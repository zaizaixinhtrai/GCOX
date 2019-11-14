package com.gcox.fansmeet.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by User on 4/6/2016.
 */
public class CustomFontButton extends Button {

    public CustomFontButton(Context context) {
        super(context);

//        (new CustomFontUtils()).applyCustomFont(this, context, null);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        (new CustomFontUtils()).applyCustomFont(this, context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        (new CustomFontUtils()).applyCustomFont(this, context, attrs);
    }
    public void setCustomFont(Context context, String font){
        (new CustomFontUtils()).applyCustomFont(this, context, font);
    }
}
