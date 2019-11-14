package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.gcox.fansmeet.R;

/**
 * Created by linh on 10/11/2016.
 */

public class BadgeImageView extends android.support.v7.widget.AppCompatImageView {
    private float badgeX; // x-coordinate
    private float badgeY;// y-coordinate
    private float textX; // x-coordinate
    private float textY;// y-coordinate
    private float badgeRadius;
    private int badgeColor;
    private boolean showBadge;
    private int badgePosition;
    private int textPosition;

    private boolean showTextNumber;
    private String textNumber;

    private Paint badgePaint;
    private Paint textNumberPaint;
    private float textNumberSize;
    private float textMarginX; // x-coordinate
    private float textMarginY;// y-coordinate
    private String fontName;

    //================ constructor methods =========================================================
    public BadgeImageView(Context context) {
        super(context);
    }

    public BadgeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public BadgeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
    }

    //================= getters and setters methods ================================================
    public void setShowBadge(boolean showBadge) {
        if (showBadge != this.showBadge) {
            this.showBadge = showBadge;
            invalidate();
        }
    }

    public boolean isShowBadge() {
        return showBadge;
    }

    public boolean isShowNumber() {
        return showTextNumber;
    }

    public void setShowNumber(boolean showTextNumber) {
        this.showTextNumber = showTextNumber;
        invalidate();
    }

    public String getTextNumber() {
        return textNumber;
    }

    public void setTextNumber(String textNumber) {
        this.textNumber = textNumber;
        reCalculateTextNumberSpecs();
        invalidate();
    }

    //================= inherited methods ==========================================================

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateSpecs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showBadge) {
            canvas.drawCircle(badgeX, badgeY, badgeRadius, badgePaint);
        }
        if (showTextNumber) {
            canvas.drawText(textNumber, textX, textY, textNumberPaint);
        }
    }

    //============ inner methods ===================================================================
    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeImageView, 0, 0);
            badgeX = a.getDimension(R.styleable.BadgeImageView_badgeX, 0);
            badgeY = a.getDimension(R.styleable.BadgeImageView_badgeY, 0);
            badgeRadius = a.getDimension(R.styleable.BadgeImageView_badgeRadius, 0);
            badgeColor = a.getColor(R.styleable.BadgeImageView_badgeColor, Color.RED);
            showBadge = a.getBoolean(R.styleable.BadgeImageView_showBadge, false);
            badgePosition = a.getInt(R.styleable.BadgeImageView_badgePosition, 0);
            showTextNumber = a.getBoolean(R.styleable.BadgeImageView_showText, false);
            textNumberSize = a.getDimensionPixelSize(R.styleable.BadgeImageView_textSize, 0);
            textMarginX = a.getDimensionPixelSize(R.styleable.BadgeImageView_textMarginX, 0);
            textMarginY = a.getDimensionPixelSize(R.styleable.BadgeImageView_textMarginY, 0);
            textPosition = a.getInt(R.styleable.BadgeImageView_textPosition, 0);
            fontName = a.getString(R.styleable.BadgeImageView_fontName);
            textNumber = a.getString(R.styleable.BadgeImageView_text);
            a.recycle();
        } else {
            badgeX = 0;
            badgeY = 0;
            badgeRadius = 0;
            badgeColor = Color.RED;
        }
        initBadgePaint();
    }

    private void initBadgePaint() {
        badgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        badgePaint.setColor(badgeColor);
        textNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textNumberPaint.setColor(Color.WHITE);
        textNumberPaint.setTextSize(textNumberSize);
        textNumberPaint.setTypeface(CustomFontUtils.selectTypeface(getContext(), fontName));
    }

    private void calculateSpecs() {
        if (showBadge) {
            reCalculateBadgeSpecs();
        }
        if (showTextNumber) {
            reCalculateTextNumberSpecs();
        }
    }

    private void reCalculateBadgeSpecs() {
        switch (badgePosition) {
            case 0://left top
                break;

            case 1://right top
                badgeX = getWidth() - getPaddingRight() - badgeX;
                break;

            case 2:// left bottom
                badgeY = getHeight() - getPaddingBottom() - badgeY;
                break;

            case 3://right bottom
                badgeX = getWidth() - getPaddingRight() - badgeX;
                badgeY = getHeight() - getPaddingBottom() - badgeY;
                break;
            default:
        }
    }

    private void reCalculateTextNumberSpecs() {
        textNumberPaint.setTextSize(textNumberSize);
        Rect textNumberBounds = new Rect();
        textNumberPaint.getTextBounds(textNumber, 0, textNumber.length(), textNumberBounds);
        switch (textPosition) {
            case 0://left top
                textX = 0;
                textY = textNumberBounds.height();
                break;
            case 1://right top
                textX = getWidth() - textNumberBounds.width() - textMarginX;
                textY = textNumberBounds.height();
                break;
            case 2:// left bottom
                textX = 0;
                textY = getHeight() - textMarginY;
                break;
            case 3://right bottom
                textX = getWidth() - textNumberBounds.width() - textMarginX;
                textY = getHeight() - textMarginY;
                break;
            case 4://center
                textX = getWidth() / 2 - textNumberBounds.width() / 2 - textMarginX;
                textY = getHeight() / 2 + textNumberBounds.height() / 2 - textMarginY;
                break;
            default:
        }
    }
}
