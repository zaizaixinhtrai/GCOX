package com.gcox.fansmeet.util;

import android.content.Context;
import android.graphics.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import timber.log.Timber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by User on 8/28/2015.
 */
public class BitmapUtil {


    // Clear bitmap
    public void clearImagePhoto(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        final int srcWidth = bitmap.getWidth();
        final int srcHeight = bitmap.getHeight();
        int width = maxSize;
        int height = maxSize;
        boolean needsResize = false;
        if (srcWidth > srcHeight) {
            if (srcWidth > maxSize) {
                needsResize = true;
                height = ((maxSize * srcHeight) / srcWidth);
            }
        } else {
            if (srcHeight > maxSize) {
                needsResize = true;
                width = ((maxSize * srcWidth) / srcHeight);
            }
        }
        if (needsResize) {
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        } else {
            return bitmap;
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static void saveBitmap(Bitmap bitmap, String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        OutputStream os = new FileOutputStream(file);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
        } finally {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            os.flush();
            os.close();
        }
    }

    public static Bitmap autoRotateBitmap(Bitmap bitmap, ExifInterface exif) {
        int rotate = getExifRotation(exif);
        if (rotate != 0) {
            bitmap = rotateBitmap(bitmap, rotate);
        }

        return bitmap;
    }

    public static void rotateAndSaveBitmap(String path) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        int rotate = getExifRotation(exif);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (rotate != 0) {
            bitmap = rotateBitmap(bitmap, rotate);
        }
        saveBitmap(bitmap, path);
    }


    private static int getExifRotation(ExifInterface exif) {
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static Bitmap getBitmapFromSDCard(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return bitmap;
    }


    public static File saveBitmapToFile(Context context, Bitmap bitmap, File file) {
        try {

            OutputStream fOut = null;
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    //here we scale bitmap and apply blur
    public static Bitmap blurRenderScript(Bitmap bitmap) {

        try {

            float scaleFactor = 8;
            int radius = 2;
            int inputWidth = bitmap.getWidth();
            int inputHeight = bitmap.getHeight();

            Bitmap overlay = Bitmap.createBitmap((int) (inputWidth / scaleFactor), (int) (inputHeight / scaleFactor), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(overlay);

            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);

            canvas.drawBitmap(bitmap, 0, 0, paint);

            //  bkg.recycle();
            overlay = new FastBlur().doBlur(overlay, radius, true);
            return getResizedBitmap(overlay, inputWidth, inputHeight, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth, boolean willDelete) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        //          if(willDelete)
        //              bm.recycle();

        return resizedBitmap;
    }

//    public static Bitmap blurImage(Bitmap bm, float scale, int radius) {
//        return BlurImage.fastblur(bm, scale, radius);
//    }

    public static Bitmap screenShot(View view) {
        return screenShot(view, 0);

    }

    public static Bitmap screenShot(View view, int background) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (background != 0) canvas.drawColor(background);
        view.draw(canvas);
        return getResizedBitmap(bitmap, 720);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        // recreate the new Bitmap
        Bitmap resizedBitmap = getResizedBitmap(image, width, height);

        //release olddata
        releaseBitmap(image);
        return resizedBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public static Uri storeAndGetUri(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        File mShareFile = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(mShareFile);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            return Uri.fromFile(mShareFile);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
