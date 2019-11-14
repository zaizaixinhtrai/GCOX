package com.gcox.fansmeet.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import timber.log.Timber;

/**
 * Created by linh on 01/06/2017.
 */

public abstract class BaseCustomView extends View {

    public BaseCustomView(Context context) {
        super(context);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //#region inherited methods ====================================================================
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec), getImprovedDefaultHeight(heightMeasureSpec));
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //#region inner methods ========================================================================

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
                return hGetMaximumHeight();

            /**
             *wrap_content:  The size that gets passed could be much larger, taking up the rest of the space. So it might
             say, “I have 411 pixels. Tell me your size that doesn’t exceed 411 pixels.” The question then to the
             programmer is: What should I return?
             */
            case MeasureSpec.AT_MOST:
                return hGetMinimumHeight(specSize);

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
                return hGetMinimumWidth(specSize);

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

    protected abstract int hGetMaximumHeight();

    protected abstract int hGetMaximumWidth();

    protected abstract int hGetMinimumHeight(int maxHeight);

    protected abstract int hGetMinimumWidth(int maxWidth);
}
