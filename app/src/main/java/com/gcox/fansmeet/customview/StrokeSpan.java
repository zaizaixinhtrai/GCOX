package com.gcox.fansmeet.customview;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import com.gcox.fansmeet.util.Utils;

/**
 * Created by linh on 05/06/2017.
 */

public class StrokeSpan extends CharacterStyle {
    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setStyle(Paint.Style.FILL_AND_STROKE);
        tp.setColor(Color.parseColor("#3f000000"));
        tp.setStrokeWidth(Utils.dpToPx(0.5f));
    }
}
