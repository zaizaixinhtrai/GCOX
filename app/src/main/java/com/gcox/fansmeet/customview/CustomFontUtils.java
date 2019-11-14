package com.gcox.fansmeet.customview;

/**
 * Created by User on 3/2/2016.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gcox.fansmeet.R;
import timber.log.Timber;

public class CustomFontUtils {

    void applyCustomFont(TextView customFontTextView, Context context, AttributeSet attrs) {

        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_appFont);
//        int indexCount = attributeArray.getIndexCount();
//        for (int i = 0; i < indexCount; ++i) {
//            final int attr = attributeArray.getIndex(i);
//            if (attr == R.styleable.CustomFontTextView_font) {
//                fontName = attributeArray.getText(attr).toString();
//                break;
//            }
//        }

        Typeface customFont = selectTypeface(context, fontName);
        if (customFont != null) {
            customFontTextView.setTypeface(customFont);
        }

        attributeArray.recycle();
    }

    public void applyCustomFont(TextView customFontTextView, Context context, String fontName){
        Typeface customFont = selectTypeface(context, fontName);
        if (customFont != null) {
            customFontTextView.setTypeface(customFont);
        }
    }

    public static Typeface selectTypeface(Context context, String fontName) {
        if (TextUtils.isEmpty(fontName)) {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }

        try {
            return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
        }catch (RuntimeException e){
            Timber.e(e);
        }
        return null;
    }
}