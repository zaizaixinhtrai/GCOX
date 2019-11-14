package com.gcox.fansmeet.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.util.BitmapUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by linh on 28/12/2017.
 */

public class GenderedCircleImageView extends CircleImageView{
    private float mGenderIconX;
    private float mGenderIconY;
    private int mGenderIconWidth;
    private int mGenderIconHeight;
    private int mGenderIconHorizontalMargin;
    private int mGenderIconVerticalMargin;
    private int mGenderIconPosition;
    private @Gender int mGender = Gender.NONE;
    private Bitmap mGenderIconBitmap;

    private Paint mGenderIconPaint;

    public GenderedCircleImageView(Context context) {
        super(context);
        constructor(context, null);
    }

    public GenderedCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor(context, attrs);
    }

    public GenderedCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        constructor(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        reCalculateGenderIconSpecs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGenderIcon(canvas);
    }

    //=========== inner methods ====================================================================
    public void setGender(String gender){
        int g;
        if (gender.equalsIgnoreCase(Constants.GENDER_SECRET)){
            g = Gender.NONE;
        }else if (gender.equalsIgnoreCase(Constants.GENDER_MALE)){
            g = Gender.MALE;
        }else if (gender.equalsIgnoreCase(Constants.GENDER_FEMALE)){
            g = Gender.FEMALE;
        }else{
            g = Gender.NONE;
        }
        setGender(g);
    }

    public void setGender(int gender) {
        mGender = gender;
        loadGenderIcon();
        int l = (int) mGenderIconX;
        int t = (int) mGenderIconY;
        int r = (int) (mGenderIconX + mGenderIconWidth);
        int b = (int) (mGenderIconY + mGenderIconHeight);
        invalidate(l, t, r, b);
    }

    @SuppressWarnings("WrongConstant")
    private void constructor(Context context, AttributeSet attrs){
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GenderedCircleImageView, 0, 0);
            mGender = a.getInt(R.styleable.GenderedCircleImageView_gender, Gender.NONE);
            mGenderIconWidth = a.getDimensionPixelSize(R.styleable.GenderedCircleImageView_genderIconWidth, 0);
            mGenderIconHeight = a.getDimensionPixelSize(R.styleable.GenderedCircleImageView_genderIconHeight, 0);
            mGenderIconHorizontalMargin = a.getDimensionPixelSize(R.styleable.GenderedCircleImageView_genderIconMarginRelativeHorizon, 0);
            mGenderIconVerticalMargin = a.getDimensionPixelSize(R.styleable.GenderedCircleImageView_genderIconMarginRelativeVertical, 0);
            mGenderIconPosition = a.getInt(R.styleable.GenderedCircleImageView_genderPosition, 0);
            a.recycle();
        }

        loadGenderIcon();
        mGenderIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void loadGenderIcon(){
        if (mGender != Gender.NONE){
            int genderIconRes = getGenderIconRes(mGender);
            mGenderIconBitmap = BitmapUtil.getResizedBitmap(BitmapFactory.decodeResource(getContext().getResources(), genderIconRes), mGenderIconWidth, mGenderIconHeight);
        }
    }

    private @DrawableRes int getGenderIconRes(@Gender int gender){
        switch (gender){
            case Gender.MALE:
                return R.drawable.ic_gender_male;

            case Gender.FEMALE:
                return R.drawable.ic_gender_female;

            case Gender.SECRET:
                return R.drawable.ic_gender_secret;
            default:
                return R.drawable.ic_gender_secret;
        }
    }

    private void reCalculateGenderIconSpecs(){
        switch (mGenderIconPosition){
            case 0://left top
                mGenderIconX += mGenderIconHorizontalMargin;
                mGenderIconY += mGenderIconVerticalMargin;
                break;

            case 1://right top
                mGenderIconX = getWidth() - getPaddingRight() - mGenderIconWidth - mGenderIconHorizontalMargin;
                mGenderIconY += mGenderIconVerticalMargin;
                break;

            case 2:// left bottom
                mGenderIconX += mGenderIconHorizontalMargin;
                mGenderIconY = getHeight() - getPaddingBottom() - mGenderIconHeight - mGenderIconVerticalMargin;
                break;

            case 3://right bottom
                mGenderIconX = getWidth() - getPaddingRight() - mGenderIconWidth - mGenderIconHorizontalMargin;
                mGenderIconY = getHeight() - getPaddingBottom() - mGenderIconHeight - mGenderIconVerticalMargin;
                break;

            default:
                break;
        }
    }

    private void drawGenderIcon(Canvas canvas){
        if (mGenderIconBitmap != null) {
            canvas.drawBitmap(mGenderIconBitmap, mGenderIconX, mGenderIconY, mGenderIconPaint);
        }
    }

    //========== inner classes =====================================================================
    @IntDef({Gender.NONE, Gender.MALE, Gender.FEMALE, Gender.SECRET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gender{
        int NONE = -1;
        int MALE = 0;
        int FEMALE = 1;
        int SECRET = 2;
    }
}
