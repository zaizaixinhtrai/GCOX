package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.Utils;

/**
 * Created by linh on 01/06/2017.
 */

public class CircularColorItem extends BaseCustomView {
    private static final int DEFAULT_FILL_RADIUS = Utils.dpToPx(10);
    private static final int DEFAULT_STROKE_WIDTH = Utils.dpToPx(2);

    private Paint mFillPaint;
    private Paint mStrokePaint;
    private int mFillColor;
    private int mStrokeColor;
    private float mFillRadius;
    private float mStrokeWidth;
    private int mMarginLeft;
    private int mMarginRight;

    private PointF mCentralPoint;

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
        invalidate();
    }

    public CircularColorItem(Context context, int color, int marginLeft, int marginRight) {
        super(context);
        mFillColor = color;
        mMarginLeft = marginLeft;
        mMarginRight = marginRight;
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.CircularColorItem, 0, R.style.stream_title_color_item_picker);
        constructor(context, a);
    }

    public CircularColorItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        constructor(context, attrs);
    }

    public CircularColorItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructor(context, attrs);
    }

    public CircularColorItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        constructor(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mMarginLeft > 0 || mMarginRight > 0) {
            ViewGroup.MarginLayoutParams margins = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
            margins.leftMargin = mMarginLeft;
            margins.rightMargin = mMarginRight;
            setLayoutParams(margins);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCentralPoint.set(w*0.5f, h*0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCentralPoint.x, mCentralPoint.y, mFillRadius, mStrokePaint);
        canvas.drawCircle(mCentralPoint.x, mCentralPoint.y, mFillRadius, mFillPaint);
    }

    @Override
    protected int hGetMaximumHeight() {
        return (int) (mFillRadius + mStrokeWidth) * 2;
    }

    @Override
    protected int hGetMaximumWidth() {
        return (int) (mFillRadius + mStrokeWidth) * 2;
    }

    @Override
    protected int hGetMinimumHeight(int maxHeight) {
        int h = (int) (mFillRadius + mStrokeWidth) * 2 + getPaddingTop() + getPaddingBottom();
        if (h < maxHeight){
            return h;
        }else{
            return maxHeight;
        }
    }

    @Override
    protected int hGetMinimumWidth(int maxWidth) {
        int w = (int) (mFillRadius + mStrokeWidth) * 2 + getPaddingLeft() + getPaddingRight();
        if (w < maxWidth){
            return w;
        }else{
            return maxWidth;
        }
    }

    private void constructor(Context context, @Nullable AttributeSet attrs) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularColorItem, 0, 0);
        constructor(context, a);
    }

    private void constructor(Context context, @Nullable TypedArray a) {
        extractAttr(context, a);
        if (a != null) {
            a.recycle();
        }
        init();
    }

    private void extractAttr(Context context, TypedArray a){
        if (a != null) {
            mFillColor = a.getColor(R.styleable.CircularColorItem_fillColor, mFillColor != 0 ? mFillColor : getResources().getColor(R.color.stream_title_color_default));
            mStrokeColor = a.getColor(R.styleable.CircularColorItem_strokeColor, getResources().getColor(R.color.stream_title_color_default));
            mFillRadius = a.getDimension(R.styleable.CircularColorItem_fillRadius, DEFAULT_FILL_RADIUS);
            mStrokeWidth = a.getDimension(R.styleable.CircularColorItem_strokeWidth, DEFAULT_STROKE_WIDTH);
        }else{
            mFillColor = getResources().getColor(R.color.stream_title_color_default);
            mStrokeColor = mFillColor;
            mFillRadius = DEFAULT_FILL_RADIUS;
            mStrokeWidth = DEFAULT_STROKE_WIDTH;
        }
    }

    private void init(){
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setColor(mFillColor);
        mFillPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setColor(mStrokeColor);

        mCentralPoint = new PointF();
    }
}
