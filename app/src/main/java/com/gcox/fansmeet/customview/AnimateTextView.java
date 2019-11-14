package com.gcox.fansmeet.customview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

/**
 * Created by thanhbc on 9/10/17.
 */

public class AnimateTextView extends CustomFontTextView {
    CharSequence mText;
    int mIndex = 0;
    long mDelay = 500; //Default 500ms delay
    int mBeginIndex = 0;
    private final Handler mHandler = new Handler();
    private final WeakRunnable characterAdder = new WeakRunnable(this);
    public AnimateTextView(Context context) {
        super(context);
    }

    public AnimateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimateTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextAndStopAnimation(String text){
        mHandler.removeCallbacks(characterAdder);
        setText(text);
    }
    public void animateText(CharSequence text) {
        mText = text;
        setText(mBeginIndex == 0 ? "" : mText.subSequence(0, mBeginIndex));
        mIndex = mBeginIndex;
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public void setIndexBegin(int index) {
        mBeginIndex = index;
    }

    void updateText() {
        setText(mText.subSequence(0, mIndex++));
        if (mIndex > mText.length()) {
            mIndex = mBeginIndex;
        }
        mHandler.postDelayed(characterAdder, mDelay);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacks(characterAdder);
        super.onDetachedFromWindow();
    }

    private final static class WeakRunnable implements Runnable {
        private final WeakReference<AnimateTextView> mAnimateTextViewWeakReference;

        WeakRunnable(AnimateTextView textView) {
            mAnimateTextViewWeakReference = new WeakReference<AnimateTextView>(textView);
        }

        @Override
        public void run() {
            final AnimateTextView view = mAnimateTextViewWeakReference.get();
            if (view != null) {
                view.updateText();
            }
        }
    }
}
