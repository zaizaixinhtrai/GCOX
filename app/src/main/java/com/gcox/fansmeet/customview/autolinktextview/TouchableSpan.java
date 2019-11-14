package com.gcox.fansmeet.customview.autolinktextview;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by thanhbc on 6/19/17.
 */

public abstract class TouchableSpan extends ClickableSpan {
    private boolean isPressed;
    private int normalTextColor;
    private int pressedTextColor;
    private boolean isUnderLineEnabled;

    public TouchableSpan() {
    }

    public TouchableSpan(int normalTextColor, int pressedTextColor, boolean isUnderLineEnabled) {
        this.normalTextColor = normalTextColor;
        this.pressedTextColor = pressedTextColor;
        this.isUnderLineEnabled = isUnderLineEnabled;
    }

    void setPressed(boolean isSelected) {
        isPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        int textColor = isPressed ? pressedTextColor : normalTextColor;
        textPaint.setColor(textColor);
        textPaint.bgColor = Color.TRANSPARENT;
        textPaint.setUnderlineText(isUnderLineEnabled);
    }
}
