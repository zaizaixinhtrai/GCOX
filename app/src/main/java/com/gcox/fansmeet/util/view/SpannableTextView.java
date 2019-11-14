package com.gcox.fansmeet.util.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.gcox.fansmeet.util.ClickableImageSpan;
import com.gcox.fansmeet.util.Utils;

/**
 * Created by linh on 20/10/2016.
 */

public class SpannableTextView extends View {
    private Layout mLayout = null;
    private Layout mStrokeLayout = null;

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mStrokeLayout != null) {
            mStrokeLayout.draw(canvas);
        }

        if (mLayout != null) {
            mLayout.draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLayout != null) {
            setMeasuredDimension(getPaddingLeft() + mLayout.getWidth(), getPaddingTop() + getPaddingBottom() + mLayout.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return updateSelection(event, (Spannable) mLayout.getText(), mLayout);
    }

    public void setLayout(Layout layout, Layout strokeLayout) {
        this.mLayout = layout;
        this.mStrokeLayout = strokeLayout;
        requestLayout();
    }

    private boolean updateSelection(MotionEvent event, Spannable buffer, Layout layout) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getPaddingLeft();
            y -= getPaddingTop();

            x += getScrollX();
            y += getScrollY();

            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            ClickableImageSpan[] imageSpans = buffer.getSpans(off, off, ClickableImageSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(this);
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else if (imageSpans.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    imageSpans[0].onClick(this);
                } else {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(imageSpans[0]),
                            buffer.getSpanEnd(imageSpans[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return false;
    }


    public static class Builder {

        private CharSequence text;
        private int textSize;
        private int textColor;
        private int bitmapSize;

        /**
         * set text content
         */
        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        /**
         * Set the text size. This value must be > 0
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Layout build() {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(textSize);
//            textPaint.setColor(textColor);
            textPaint.setAntiAlias(true);
//            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setShadowLayer(2f, 1, 1, Color.parseColor("#7F000000"));
            return new StaticLayout(text, textPaint, getTextWidth(textPaint, text),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        }

        private int getTextWidth(TextPaint textPaint, CharSequence text) {

            int textWidth = (int) StaticLayout.getDesiredWidth(text, textPaint);
            int screenWidth = Utils.getScreenWidth() - Utils.dpToPx(10);
            return textWidth >= screenWidth ? screenWidth : textWidth;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public void setBitmapSize(int bitmapSize) {
            this.bitmapSize = bitmapSize;
        }
    }
}
