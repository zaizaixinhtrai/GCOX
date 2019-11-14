package com.gcox.fansmeet.util;

import android.util.Log;
import com.gcox.fansmeet.common.Constants;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sonnguyen on 11/18/15.
 */
public class DownloadVideos {

    private static DownloadVideos ourInstance = new DownloadVideos();

    public static DownloadVideos getInstance() {
        return ourInstance;
    }

    private DownloadVideos() {
    }


    public interface IDownloadListener {
        public void successful(String filePath);

        public void fail();
    }

    public interface IFileAlreadyDownloadedListener {
        public void needToDownload(boolean isNeedToDownload, String localVideoPath);
    }

    public void downloadVideoFile(String urlFile, IDownloadListener iDownloadListener) {
        new Thread(new DownloadFileRunnable(urlFile, iDownloadListener)).start();
    }

    String rootDir = Constants.VIDEO_CACHE_FOLDER;


    /**
     * Return local video file
     *
     * @param videoUrl
     * @return
     */
    public void isVideoAlreadyDownloaded(final String videoUrl, final IFileAlreadyDownloadedListener fileAlreadyDownloadedListener) {
        if (videoUrl == null || videoUrl.isEmpty()) {
            return;
        }

        new Thread(() -> {
            File dir = new File(rootDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = videoUrl.substring(videoUrl.lastIndexOf('/'));
            File videoLocalFile = new File(rootDir, fileName);
            if (videoLocalFile.exists()) {
                long oldFileSize = videoLocalFile.length();

                try {
                    URL url = new URL(videoUrl);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // this will be useful so that you can show a typical 0-100% progress bar
                    int fileLength = connection.getContentLength();

                    if (oldFileSize < fileLength) {
                        fileAlreadyDownloadedListener.needToDownload(true, Constants.EMPTY_STRING);
                    } else {
                        fileAlreadyDownloadedListener.needToDownload(false, videoLocalFile.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    fileAlreadyDownloadedListener.needToDownload(true, Constants.EMPTY_STRING);
                }

            } else {
                fileAlreadyDownloadedListener.needToDownload(true, Constants.EMPTY_STRING);
            }
        }).start();

    }


    private static class DownloadFileRunnable implements Runnable {
        String urlFile;
        String fileLocal = "";
        IDownloadListener iDownloadListener;
        String rootDir = Constants.VIDEO_CACHE_FOLDER;

        public DownloadFileRunnable(String urlFile, IDownloadListener iDownloadListener) {
            this.urlFile = urlFile;
            this.iDownloadListener = iDownloadListener;

        }

        @Override
        public void run() {

            try {
                fileLocal = downloadFile(urlFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(DownloadVideos.class.getName(), "File downloaded: " + fileLocal);
            if (fileLocal.isEmpty()) {
                iDownloadListener.fail();
            } else {
                iDownloadListener.successful(fileLocal);
            }

        }

        private String downloadFile(String urlFile) throws IOException {
            String local;

            File dir = new File(rootDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = urlFile.substring(urlFile.lastIndexOf('/'));
            File videoFile = new File(rootDir, fileName);
            local = videoFile.toString();


            long oldFileSize = videoFile.length();
            Log.d(DownloadVideos.class.getName(), "local: " + local);
            Log.d(DownloadVideos.class.getName(), "Local file size: " + oldFileSize);
            OutputStream output = null;
            URL url = new URL(urlFile);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            Log.d(DownloadVideos.class.getName(), "Remote: " + urlFile);
            Log.d(DownloadVideos.class.getName(), "Remote file size: " + fileLength);
            try (InputStream input = new BufferedInputStream(url.openStream())) {
                if (!videoFile.exists() || (videoFile.exists() && (oldFileSize < fileLength))) {
                    Log.d(DownloadVideos.class.getName(), "Start download file...: " + urlFile);
                    // download the file
                    output = new FileOutputStream(local);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....

                        output.write(data, 0, count);
                    }
                    Log.d(DownloadVideos.class.getName(), "Downloaded file size: " + total);

                    output.flush();
                    output.close();
                    input.close();

                } else {
                    Log.d(DownloadVideos.class.getName(), "Do not need to download");
                    local = videoFile.toString();
                }
            } catch (Exception e) {
                Log.d(DownloadVideos.class.getName(), e.toString());

                local = "";
                e.printStackTrace();
            } finally {
                if (output != null) output.close();
            }

            return local;
        }
    }
}
