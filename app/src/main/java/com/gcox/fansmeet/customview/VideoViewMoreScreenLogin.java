package com.gcox.fansmeet.customview;

import android.content.Context;
import android.util.AttributeSet;
import com.gcox.fansmeet.customview.ScalableVideo.ScalableVideoView;

/**
 * Created by User on 3/10/2016.
 */
public class VideoViewMoreScreenLogin extends ScalableVideoView {
    public VideoViewMoreScreenLogin(Context context) {
        super(context);
    }

    public VideoViewMoreScreenLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoViewMoreScreenLogin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // or you can use this if you want the square to use height as it basis
//        setMeasuredDimension(getMeasuredWidth() + PixelUtil.dpToPx(getContext(), Resources.getSystem().getDisplayMetrics().widthPixels / 3),
//                getMeasuredHeight());
    }
}
