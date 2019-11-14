package com.gcox.fansmeet.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import timber.log.Timber;

/**
 * Created by User on 9/25/2015.
 */
public class DownloadFile {
    private DownloadFile() {
        throw new IllegalStateException("Utility class");
    }

    public static Bitmap downloadImageFromURL(String url) {
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(url).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Timber.e(e);
        }
        return bitmap;
    }


}
