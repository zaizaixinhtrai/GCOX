package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.gcox.fansmeet.R;
import timber.log.Timber;

/**
 * Created by linh on 20/02/2017.
 */

public class OnlyDrawableClickableTextView extends CustomFontTextView implements View.OnTouchListener{
    private final static int DRAWABLE_CLICKABLE_NULL = 0x0;
    private final static int DRAWABLE_CLICKABLE_LEFT = 0x1;
    private final static int DRAWABLE_CLICKABLE_TOP = 0x2;
    private final static int DRAWABLE_CLICKABLE_RIGHT = 0x4;
    private final static int DRAWABLE_CLICKABLE_BOTTOM = 0x8;
    private int drawableClickable = 0x0;

    public OnlyDrawableClickableTextView(Context context) {
        super(context);
        constructor(context, null, 0);
    }

    public OnlyDrawableClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlyDrawableClickableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        constructor(context, attrs, defStyle);
    }

    private void constructor(Context context, AttributeSet attrs, int defStyle){
        TypedArray attributeArray = context.obtainStyledAttributes( attrs, R.styleable.OnlyDrawableClickableTextView);
        drawableClickable = attributeArray.getInteger(R.styleable.OnlyDrawableClickableTextView_drawable_clickable, DRAWABLE_CLICKABLE_NULL);

        attributeArray.recycle();

        setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {

            if ((drawableClickable & DRAWABLE_CLICKABLE_LEFT) == DRAWABLE_CLICKABLE_LEFT){
                if(event.getX() >= getTotalPaddingLeft()) {
                    return true;
                }
            }

            if ((drawableClickable & DRAWABLE_CLICKABLE_TOP) == DRAWABLE_CLICKABLE_TOP){
                Drawable drawableTop = getCompoundDrawables()[1];
                int drawableWidth = drawableTop.getIntrinsicWidth();
//                int drawableHeight = drawableTop.getIntrinsicHeight();
                int radDrawable = (int) Math.ceil(drawableWidth/2);
                int widthOfTextView = getWidth();
                int leftDrawableTop = widthOfTextView/2 - radDrawable;
                int rightDrawableTop = widthOfTextView/2 + radDrawable;
                if(event.getY() >= getTotalPaddingTop()) {// outside y axis
                    Timber.d("prevent clicked");
                    return true;
                }
                if (event.getX() <= leftDrawableTop || event.getX() >= rightDrawableTop) {// outside by x axis
                    Timber.d("prevent clicked");
                    return true;
                }
            }

            if ((drawableClickable & DRAWABLE_CLICKABLE_RIGHT) == DRAWABLE_CLICKABLE_RIGHT){
                if(event.getX() <= getTotalPaddingRight()) {
                    return true;
                }
            }

            if ((drawableClickable & DRAWABLE_CLICKABLE_BOTTOM) == DRAWABLE_CLICKABLE_BOTTOM){
                if(event.getY() <= getTotalPaddingBottom()) {
                    return true;
                }
            }


        }
        return false;
    }
}
