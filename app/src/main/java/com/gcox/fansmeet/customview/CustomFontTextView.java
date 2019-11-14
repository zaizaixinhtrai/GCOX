package com.gcox.fansmeet.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by User on 3/2/2016.
 */
public class CustomFontTextView extends CompoundDrawableTextView {

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        (new CustomFontUtils()).applyCustomFont(this, context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        (new CustomFontUtils()).applyCustomFont(this, context, attrs);
    }

    public void setCustomFont(Context context, String font) {
        (new CustomFontUtils()).applyCustomFont(this, context, font);
    }
}
