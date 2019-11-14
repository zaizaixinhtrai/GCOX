package com.gcox.fansmeet.util.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by sonnguyen on 11/17/16.
 */

public class HashTag extends ClickableSpan {
    Context context;

    public HashTag(Context ctx) {
        super();
        context = ctx;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#f05c56"));
//        ds.setARGB(240, 92, 86, 1);
    }

    @Override
    public void onClick(View widget) {

    }
}
