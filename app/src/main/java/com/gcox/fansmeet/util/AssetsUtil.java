package com.gcox.fansmeet.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by User on 3/1/2016.
 */
public class AssetsUtil {
    private AssetsUtil() {
    }

    public static Typeface getFont(Context context, String fontName) {

        if (StringUtil.isNullOrEmptyString(fontName)) {
            return null;
        }

        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }
}
