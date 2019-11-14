package com.gcox.fansmeet.util.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by linh on 19/12/2017.
 */

public class CircleTransformation extends BitmapTransformation {
    private int STROKE_WIDTH = 6;

    public CircleTransformation(int STROKE_WIDTH) {
        this.STROKE_WIDTH = STROKE_WIDTH;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(toTransform, x, y, size, size);
        if (squaredBitmap != toTransform) {
            toTransform.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, toTransform.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint avatarPaint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        avatarPaint.setShader(shader);
        Paint outlinePaint = new Paint();
        outlinePaint.setColor(Color.WHITE);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(STROKE_WIDTH);
        outlinePaint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, avatarPaint);
        canvas.drawCircle(r, r, r - STROKE_WIDTH / 2, outlinePaint);
//        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
