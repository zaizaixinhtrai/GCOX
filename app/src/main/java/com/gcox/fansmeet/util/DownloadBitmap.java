package com.gcox.fansmeet.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import com.gcox.fansmeet.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by User on 5/17/2016.
 */
public class DownloadBitmap {

    private String fileRoot = Constants.APPSTERS_IMAGE_SHARE;

    private static DownloadBitmap ourInstance = new DownloadBitmap();

    public static DownloadBitmap getInstance() {
        return ourInstance;
    }

    public boolean isImageAlreadyDownloaded(File file) {
        return true;
    }

    public void downloadBitmap(Context context, String urlImage, String wallpaperDirectory, String filename, IDownloadListener iDownloadListener) {

        new DownloadImage(context, urlImage, wallpaperDirectory, filename, iDownloadListener).execute();

    }

    private class DownloadImage extends AsyncTask<String, String, String> {

        String picture;
        IDownloadListener iDownloadListener;
        String wallpaperDirectory;
        String filename;
        Context context;

        public DownloadImage(Context context, String picture, String wallpaperDirectory, String filename, IDownloadListener iDownloadListener) {
            this.context = context;
            this.picture = picture;
            this.iDownloadListener = iDownloadListener;
            this.wallpaperDirectory = wallpaperDirectory;
            this.filename = filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String pathFile = downloadBitmap(picture, wallpaperDirectory, filename);
            return pathFile;
        }

        @Override
        protected void onPostExecute(String pathFile) {
            super.onPostExecute(pathFile);

            if (StringUtil.isNullOrEmptyString(pathFile)) {
                iDownloadListener.fail();
            } else {
                iDownloadListener.successful(pathFile);
            }

        }

        private String downloadBitmap(String urlImage, String directory, String filename) {
            InputStream input = null;
            String outputFilePath = null;
            File outputFile = null;
            try {
                // create a File object for the parent directory
                File wallpaperDirectory = new File(directory);

                if (!wallpaperDirectory.exists()) {
                    wallpaperDirectory.mkdirs();
                }

                outputFile = new File(wallpaperDirectory.getAbsolutePath(), filename);

                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                URL url = new URL(urlImage);
                input = url.openStream();
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()
                OutputStream output = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }

                outputFilePath = FileUtility.getPath(context, Uri.fromFile(outputFile));
            } catch (Exception e) {
                e.printStackTrace();

                outputFilePath = null;
            } finally {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    outputFilePath = null;
                }
            }

            return outputFilePath;
        }
    }

    /**
     * Return local video file
     *
     * @param
     * @return
     */
    public boolean isNeedDownloadedImage(final String RootDir, final String filename) {
        long oldFileSize = 0;

        File dir = new File(RootDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File videoLocalFile = new File(RootDir, filename);
        if (videoLocalFile.exists()) {
            oldFileSize = videoLocalFile.length();
        }

        if (oldFileSize <= 0) {

            return true;
        } else {
            return false;

        }

    }

    public void deleteAllFile() {
        File yourDir = new File(fileRoot);

        if (!yourDir.exists()) {
            yourDir.mkdirs();
        }

        if (yourDir.listFiles() == null) return;

        int filesNumber = yourDir.listFiles().length;
        if (filesNumber > Constants.MAX_IMAGE_SHARE_CACHE_NUMBER) {
            File[] files = yourDir.listFiles();
            Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

            for (int i = 0; i < files.length - Constants.MAX_IMAGE_SHARE_CACHE_NUMBER; i++) {
                FileUtility.deleteFile(files[i]);
            }

        }
    }

    public interface IDownloadListener {
        void successful(String filePath);

        void fail();
    }

//    public interface IFileAlreadyDownloadedListener {
//        void needToDownload(boolean isNeedToDownload, String localFile);
//    }
}
