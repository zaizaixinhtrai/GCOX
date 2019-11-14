package com.gcox.fansmeet.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.glide.GlideApp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by linh on 19/12/2017.
 */

public class ImageLoaderUtil {

    public static void displayUserImage(Context context, Object url, ImageView imageView, @ScaleType int scaleType) {
        displayUserImage(context, url, imageView, scaleType, true, 0, 0, null, null);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView) {
        displayUserImage(context, url, imageView, ScaleType.NONE, true, 0, 0, null, null);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView, int width, int height, Transformation<Bitmap> transformation) {
        displayUserImage(context, url, imageView,ScaleType.NONE, true, width, height, transformation, null);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView, Transformation<Bitmap> transformation) {
        displayUserImage(context, url, imageView,ScaleType.NONE, true, 0, 0, transformation, null);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView, @ScaleType int scaleType, boolean placeHolder, int width, int height, ImageLoaderCallback callback) {
        displayUserImage(context, url, imageView, scaleType, placeHolder, width, height, null, callback);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView, boolean placeHolder, int width, int height, ImageLoaderCallback callback) {
        displayUserImage(context, url, imageView,ScaleType.NONE, placeHolder, width, height, null, callback);
    }
    public static void displayUserImage(Context context, Object url, ImageView imageView, boolean placeHolder, ImageLoaderCallback callback) {
        displayUserImage(context, url, imageView,ScaleType.NONE, placeHolder, 0, 0, null, callback);
    }

    public static void displayUserImage(Context context, Object url, ImageView imageView,@ScaleType int scaleType, boolean placeHolder, ImageLoaderCallback callback) {
        displayUserImage(context, url, imageView,scaleType, placeHolder, 0, 0, null, callback);
    }


    private static void displayUserImage(Context context, Object url, ImageView imageView,@ScaleType int scaleType, boolean placeHolder, int width, int height, Transformation<Bitmap> transformation, ImageLoaderCallback callback) {
        int holder = 0;
        if (placeHolder) {
            holder = R.drawable.user_image_default;
        }
        displayImage(context, url, imageView,scaleType, holder, width, height, transformation, callback);
    }

    public static void displayMediaImage(Context context, Object url, boolean placeholder, ImageView imageView) {
        displayMediaImage(context, url, imageView,ScaleType.NONE, placeholder, 0, 0);
    }

    public static void displayMediaImage(Context context, Object url, ImageView imageView) {
        displayMediaImage(context, url, imageView,ScaleType.NONE, false, 0, 0);
    }

    public static void displayMediaImage(Context context, Object url, ImageView imageView, int width, int height) {
        displayMediaImage(context, url, imageView,ScaleType.NONE, false, width, height);
    }
    public static void displayMediaImage(Context context, Object url, ImageView imageView,@ScaleType int scaleType, int width, int height) {
        displayMediaImage(context, url, imageView,scaleType, false, width, height);
    }

    private static void displayMediaImage(Context context, Object url, ImageView imageView,@ScaleType int scaleType, boolean placeHolder, int width, int height) {
        int holder = 0;
        if (placeHolder) {
            holder = R.drawable.user_image_default;
        }
        displayImage(context, url, imageView,scaleType, holder, width, height, null, null);
    }


    @SuppressWarnings("CheckResult")
    private static void displayImage(Context context, Object url, ImageView imageView,@ScaleType int scaleType, @DrawableRes int imageHolder , int width, int height, Transformation<Bitmap> transformation, ImageLoaderCallback callback) {
        if (context == null) return;
        RequestOptions requestOptions= new RequestOptions();
        if (imageHolder != 0){
            requestOptions.placeholder(imageHolder).error(imageHolder).fallback(imageHolder);
        }

        if (width != 0 && height != 0) {
            requestOptions.override(width, height);
        }

        if (transformation != null){
            requestOptions.transform(transformation);
        }

        //none is default scale type to respect image size
        if(scaleType!= ScaleType.NONE){
            requestOptions = applyScaleType(requestOptions,scaleType);
        }

        GlideApp.with(context)
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callback != null)
                            callback.onFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (callback != null)
                            callback.onSuccess(((BitmapDrawable)resource).getBitmap());
                        return false;
                    }
                })
                .into(imageView);
    }

    private static RequestOptions applyScaleType(@NonNull RequestOptions oldRequest, @ScaleType int scaleType) {
        RequestOptions requestOptions = oldRequest.clone();
        if(scaleType== ScaleType.NONE) return requestOptions; //will never happen
        switch (scaleType) {
            case ScaleType.CENTER_CROP:
                return requestOptions.centerCrop();
            case ScaleType.CENTER_INSIDE:
                return requestOptions.centerInside();
            case ScaleType.FIT_CENTER:
                return requestOptions.fitCenter();
            default:
                return requestOptions;
        }
    }

    public interface ImageLoaderCallback{
        void onFailed(Exception e);
        void onSuccess(Bitmap bitmap);
    }

    @IntDef({ScaleType.NONE, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE, ScaleType.FIT_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType{
        int NONE = 0;
        int CENTER_CROP = 1;
        int CENTER_INSIDE = 2;
        int FIT_CENTER = 3;
    }
}
