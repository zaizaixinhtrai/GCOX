package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.gcox.fansmeet.R;

/**
 * Created by linh on 19/09/2017.
 */

public class CompoundDrawableTextView extends AppCompatTextView {

    private int mDrawableWidth;
    private int mDrawableHeight;
    private int mDrawableStartWidth;
    private int mDrawableStartHeight;
    private int mDrawableEndWidth;
    private int mDrawableEndHeight;
    private int mDrawableTopWidth;
    private int mDrawableTopHeight;
    private int mDrawableBottomWidth;
    private int mDrawableBottomHeight;

    public CompoundDrawableTextView(Context context) {
        super(context);
    }

    public CompoundDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public CompoundDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CompoundDrawableTextView, defStyleAttr, defStyleRes);

        try {
            mDrawableWidth = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_width, -1);
            mDrawableHeight = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_height, -1);
            mDrawableStartWidth = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_start_width, mDrawableWidth);
            mDrawableStartHeight = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_start_height, mDrawableHeight);
            mDrawableEndWidth = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_end_width, mDrawableWidth);
            mDrawableEndHeight = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_end_height, mDrawableHeight);
            mDrawableTopWidth = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_top_width, mDrawableWidth);
            mDrawableTopHeight = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_top_height, mDrawableHeight);
            mDrawableBottomWidth = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_bottom_width, mDrawableWidth);
            mDrawableBottomHeight = array.getDimensionPixelSize(R.styleable.CompoundDrawableTextView_drawable_bottom_height, mDrawableHeight);
        } finally {
            array.recycle();
        }
        if (shouldResizeDrawableCompound()) {
            initCompoundDrawableSize();
        }
    }

    private void initCompoundDrawableSize() {
        setCompoundDrawablesRelative(getCompoundDrawableStart(), getCompoundDrawableTop(), getCompoundDrawableEnd(), getCompoundDrawableBottom());
    }

    private Drawable getCompoundDrawableStart(){
        Drawable drawable = getCompoundDrawablesRelative()[0];
        if (drawable == null) drawable = getCompoundDrawables()[0];
        return resizeDrawable(drawable, mDrawableStartWidth, mDrawableStartHeight);
    }
    private Drawable getCompoundDrawableTop(){
            Drawable drawable = getCompoundDrawablesRelative()[1];
        if (drawable == null) drawable = getCompoundDrawables()[1];
        return resizeDrawable(drawable, mDrawableTopWidth, mDrawableTopHeight);
    }
    private Drawable getCompoundDrawableEnd(){
        Drawable drawable = getCompoundDrawablesRelative()[2];
        if (drawable == null) drawable = getCompoundDrawables()[2];
        return resizeDrawable(drawable, mDrawableEndWidth, mDrawableEndHeight);
    }

    private Drawable getCompoundDrawableBottom(){
        Drawable drawable = getCompoundDrawablesRelative()[3];
        if (drawable == null) drawable = getCompoundDrawables()[3];
        return resizeDrawable(drawable, mDrawableBottomWidth, mDrawableBottomHeight);
    }

    public void setCustomDrawableStart(@DrawableRes int resourceStart){
        if (resourceStart != 0){
            Drawable drawableStart = ContextCompat.getDrawable(this.getContext(), resourceStart);
            drawableStart.setBounds(0, 0, drawableStart.getIntrinsicWidth(), drawableStart.getIntrinsicHeight());
            drawableStart = resizeDrawable(drawableStart, mDrawableStartWidth, mDrawableStartHeight);
            this.setCompoundDrawablesRelative(drawableStart, null, null, null);
        }
    }

    private Drawable resizeDrawable(Drawable drawable, int expectWidth, int expectHeight){
        if (drawable == null || (expectWidth < 0 && expectHeight < 0)) {
            return drawable;
        }
        Rect realBounds = drawable.getBounds();
        float scaleFactor = realBounds.height() / (float) realBounds.width();
        float drawableWidth = realBounds.width();
        float drawableHeight = realBounds.height();

        if (expectWidth > 0 && drawableWidth != expectWidth) {
            drawableWidth = expectWidth;
            drawableHeight = drawableWidth * scaleFactor;
        }
        if (expectHeight > 0 && drawableHeight != expectHeight) {
            drawableHeight = expectHeight;
            drawableWidth = drawableHeight / scaleFactor;
        }

        realBounds.right = realBounds.left + Math.round(drawableWidth);
        realBounds.bottom = realBounds.top + Math.round(drawableHeight);

        drawable.setBounds(realBounds);
        return drawable;
    }

    private boolean shouldResizeDrawableCompound(){
        return true;
    }
}
