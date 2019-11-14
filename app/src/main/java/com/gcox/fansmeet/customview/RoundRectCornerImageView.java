package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.Utils;

/**
 * Created by linh on 28/08/2017.<br
 * Updated by DatTN on 29/10/2018
 */

public class RoundRectCornerImageView extends android.support.v7.widget.AppCompatImageView {
    private float radius = 0F;
    private Path path;
    private RectF rect;

    public RoundRectCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundRectCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public RoundRectCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundRectCornerImageView, 0, 0);
            radius = a.getDimension(R.styleable.RoundRectCornerImageView_radius, 0);
            a.recycle();
        } else {
            radius = Utils.dpToPx(3);
        }
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }
}
