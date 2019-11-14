package com.gcox.fansmeet.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Ngoc on 9/1/2015.
 */
public class VideoUtil {

    private static final int BITMAP_THUMBNAIL_WIDTH = 800;
    private static final int BITMAP_THUMBNAIL_HEIGHT = 800;
    private static final int MAX_VIDEOS_SIZE = 600;

    public boolean checkDurationVideo(Activity activity, Uri mVideoURI) {

        Cursor cursor = MediaStore.Video
                .query(activity.getContentResolver(),
                        mVideoURI,
                        new String[]{MediaStore.Video.VideoColumns.DURATION});
        cursor.moveToFirst();

        int duration = cursor.getInt(cursor.getColumnIndex("duration"));
        return duration / 1000 <= 15;
    }

    public static String getRealPathFromURI(Uri contentURI, Activity activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bmThumbnail;

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);

        return bmThumbnail;
    }

    /**
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     *
     * @param filePath the path of video file
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;

        // Scale down the bitmap if it's too large.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        int min = Math.min(width, height);
        if (min >= BITMAP_THUMBNAIL_WIDTH) {

            int diff = 0;
            if (width >= height) {

                diff = width / 2 - height / 2;

                bitmap = Bitmap.createBitmap(
                        bitmap,
                        diff + min / 2 - BITMAP_THUMBNAIL_WIDTH / 2,
                        min / 2 - BITMAP_THUMBNAIL_WIDTH / 2,
                        BITMAP_THUMBNAIL_WIDTH,
                        BITMAP_THUMBNAIL_HEIGHT
                );

            } else {

                diff = height / 2 - width / 2;
                bitmap = Bitmap.createBitmap(
                        bitmap,
                        min / 2 - BITMAP_THUMBNAIL_WIDTH / 2,
                        diff + min / 2 - BITMAP_THUMBNAIL_WIDTH / 2,
                        BITMAP_THUMBNAIL_WIDTH,
                        BITMAP_THUMBNAIL_HEIGHT
                );
            }
        }

        Log.e("final width", bitmap.getWidth() + "");
        Log.e("final height", bitmap.getHeight() + "");

        return bitmap;
    }

    public static int[] getWidthHeightOfVideo(String videoPath){
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(videoPath);
        int height = Integer.parseInt(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        int width = Integer.parseInt(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        metaRetriever.release();
        int[] size = new int[2];
        size[0] = width;
        size[1] = height;
        return size;

    }
    public static int[] getSizeVideosSquare(String videoPath){
        int[] sizeVideoWidthHeight = getWidthHeightOfVideo(videoPath);
        int width = sizeVideoWidthHeight[0];
        int height = sizeVideoWidthHeight[1];
        int maxSize = width;
        if(width < height){
            maxSize = height;
        }
        while (maxSize > MAX_VIDEOS_SIZE) {
            width = width / 2;
            height = height / 2;
            maxSize = width;
            if(width < height){
                maxSize = height;
            }
        }
        int[] size = new int[2];
        size[0] = width;
        size[1] = height;
        return  size;
    }
}
