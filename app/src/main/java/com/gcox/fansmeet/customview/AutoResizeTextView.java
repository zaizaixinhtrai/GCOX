package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.gcox.fansmeet.R;
import timber.log.Timber;

/**
 * Created by linh on 31/10/2017.
 */

public class AutoResizeTextView extends CustomFontTextView {

    private int mMinTextSize;
    private int mMaxTextSize;

    public AutoResizeTextView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int improvedWidth = getImprovedDefaultWidth(widthMeasureSpec);
//        int improvedHeight = getImprovedDefaultHeight(heightMeasureSpec);
//        setMeasuredDimension(improvedWidth, improvedHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (shouldResizeText()) {
            int bestTextSize = getBestTextSize(getText().toString(), getMeasuredWidth());
            if (bestTextSize != (int)getTextSize()) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, bestTextSize);
                Timber.d("set new text size %d", bestTextSize);
            }
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        Rect bound = new Rect();
        measureTextBySize(getText().toString(), mMaxTextSize, bound);
        return bound.left + bound.width() + getPaddingStart() + getPaddingEnd();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mMaxTextSize;
    }

    //======== inner methods =======================================================================
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoResizeTextView, defStyleAttr, defStyleRes);
        mMinTextSize = array.getDimensionPixelSize(R.styleable.AutoResizeTextView_minTextSize, 0);
        mMaxTextSize = array.getDimensionPixelSize(R.styleable.AutoResizeTextView_maxTextSize, 0);
        array.recycle();
    }

    private boolean shouldResizeText(){
        return getMaxLines() == 1 && mMinTextSize > 0 && mMaxTextSize > mMinTextSize;
    }

    private int getBestTextSize(String text, int improvedWidth){
        Rect bound = new Rect();
        measureTextBySize(text, mMaxTextSize, bound);
        int bestTextSize = mMaxTextSize;
        while (bound.width() > improvedWidth){
            bestTextSize--;
            measureTextBySize(text, bestTextSize, bound);
            if (bestTextSize == mMinTextSize){
                break;
            }
        }
        Timber.d("live title text size %d", bestTextSize);
        return bestTextSize;
    }

    private Rect measureTextBySize(String text, int textSize, Rect out){
        TextPaint paint1 = new TextPaint();
        paint1.set(getPaint());
        paint1.setTextSize(textSize);
        paint1.getTextBounds(text, 0, text.length(), out);
        return out;
    }



    private int getImprovedDefaultHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            /**
             *  Documentation says that this mode is passed in when the layout wants to
             know what the true size is. True size could be as big as it could be; layout will likely then scroll it.
             With that thought, we have returned the maximum size for our view.
             */
            case MeasureSpec.UNSPECIFIED:
                Timber.e("UNSPECIFIED");
                 return hGetMaximumHeight();

            /**
             *wrap_content:  The size that gets passed could be much larger, taking up the rest of the space. So it might
             say, “I have 411 pixels. Tell me your size that doesn’t exceed 411 pixels.” The question then to the
             programmer is: What should I return?
             */
            case MeasureSpec.AT_MOST:
                int suggestedMin = getSuggestedMinimumHeight();
                Timber.e("AT_MOST %d", ((specSize < suggestedMin) ? specSize : suggestedMin));
                return (specSize < suggestedMin) ? specSize : suggestedMin;

            /**
             *  match_parent: the size will be equal parent's size
             *  exact pixels: specified size which is set
             */
            case MeasureSpec.EXACTLY:
                Timber.e("EXACTLY");
                return specSize;
        }
        //you shouldn't come here
        Timber.d("unknown specmode");
        return specSize;
    }

    private int getImprovedDefaultWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return hGetMaximumWidth();

            /**
             * wrap_content:  The size that gets passed could be much larger, taking up the rest of the space. So it might
             say, “I have 411 pixels. Tell me your size that doesn’t exceed 411 pixels.” The question then to the
             programmer is: What should I return?
             */
            case MeasureSpec.AT_MOST:
                int suggestedMin = getSuggestedMinimumWidth();
                return (specSize < suggestedMin) ? specSize : suggestedMin;

            /**
             *  match_parent: the size will be equal parent's size
             *  exact pixels: specified size which is set
             */
            case MeasureSpec.EXACTLY:
                return specSize;
        }
        //you shouldn't come here
        Timber.d("unknown specmode");
        return specSize;
    }

    private int hGetMaximumHeight() {
        return mMaxTextSize;
    }

    private int hGetMaximumWidth() {
        Rect bound = new Rect();
        measureTextBySize(getText().toString(), mMaxTextSize, bound);
        return bound.bottom + bound.width() + getPaddingStart() + getPaddingEnd();
    }

}