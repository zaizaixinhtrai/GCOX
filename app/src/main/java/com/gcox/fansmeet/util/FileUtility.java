package com.gcox.fansmeet.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import com.gcox.fansmeet.common.Constants;
import timber.log.Timber;

public class FileUtility {

    static String TAG = "FileUtility";
    private static final String IMAGE_DIRECTORY_NAME = "Appster/Picture";
    private static final String VIDEO_DIRECTORY_NAME = "Appster/Video";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE_CROPPED = 3;

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_VIDEO_TRIMMED = 4;
    Context context;

    public FileUtility(Context context) {
        this.context = context;
    }

    public static Boolean writeToSD(String text, String filename) throws IOException {
        java.util.Date currentdate = new java.util.Date();
        filename = filename
                + DateHelper.parse(currentdate, "yyyy_mm_dd HH:MM:SS");
        Boolean write_successful = false;
        File root = null;
        root = Environment.getExternalStorageDirectory();
        // check for SDcard

        Log.i(TAG, "path.." + root.getAbsolutePath());
        File fileDir = new File(root.getAbsolutePath() + "/AppsterLog");
        fileDir.mkdirs();
        if (root.canWrite()) {
            File file = new File(fileDir, filename + ".txt");
            try (FileWriter filewriter = new FileWriter(file); BufferedWriter out = new BufferedWriter(filewriter)) {
                // check sdcard permission
                out.write(text);
                out.close();
                write_successful = true;

            } catch (IOException e) {
                Log.e("ERROR:---",
                        "Could not write file to SDCard" + e.getMessage());
                write_successful = false;
            }
        }
        return write_successful;
    }

    public String readFromSD() throws IOException {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "samplefile.txt");
        StringBuilder text = new StringBuilder();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        return text.toString();
    }

    @SuppressWarnings("static-access")
    public Boolean writeToSandBox(String text) {
        Boolean write_successful = false;
        try {
            FileOutputStream fOut = context.openFileOutput("samplefile.txt",
                    context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(text);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            write_successful = false;
        }
        return write_successful;
    }

    public String readFromSandBox() throws IOException {
        String str = "";
        StringBuilder new_str = new StringBuilder();
        try (FileInputStream fIn = context.openFileInput("samplefile.txt")) {
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(isr);

            while ((str = br.readLine()) != null) {
                new_str.append(str);
            }
        }
        return new_str.toString();
    }


    private static final boolean DEBUG = false; // Set to true to enable logging

    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";

    public static final String HIDDEN_PREFIX = ".";

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf('.');
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {
        if (file == null) return "application/octet-stream";
        String extension = getExtension(file.getName());

        if (extension != null && extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is {@link LocalStorageProvider}.
     * @author paulburke
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isGoogleVideosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath(final Context context, final Uri uri) {

        if (DEBUG)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            if (isGoogleVideosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get Google photo uri example content://com.google.android.apps.photos.contentprovider/0/1/mediakey%3A%2FAF1QipMObgoK_wDY66gu0QkMAi/ORIGINAL/NONE/114919
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getGoogleFilePath(final Context context, final Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String result = getDataColumn(context, uri, null, null); //
            if (TextUtils.isEmpty(result))
                if (uri.getAuthority().contains("com.google.android")) {
                    try {
                        File localFile = createFile(context, null);
                        FileInputStream remoteFile = getSourceStream(context, uri);
                        if (copyToFile(remoteFile, localFile))
                            result = localFile.getAbsolutePath();
                        remoteFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            return result;
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    private static boolean copyToFile(InputStream inputStream, File destFile) {
        if (inputStream == null || destFile == null) return false;
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String getTimestamp() {
        try {
            return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        } catch (RuntimeException e) {
            return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        }
    }

    private static File createFile(Context context, String imageFileName) throws IOException {
        if (TextUtils.isEmpty(imageFileName))
            imageFileName = getTimestamp(); // make random filename if you want.

        final File root;
        root = context.getExternalCacheDir();

        if (root != null && !root.exists())
            root.mkdirs();
        return new File(root, imageFileName);
    }


    private static FileInputStream getSourceStream(Context context, Uri u) throws FileNotFoundException {
        FileInputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(u, "r");
            FileDescriptor fileDescriptor = null;
            if (parcelFileDescriptor != null) {
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                out = new FileInputStream(fileDescriptor);
            }
        } else {
            out = (FileInputStream) context.getContentResolver().openInputStream(u);
        }
        return out;
    }


    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail");

        if (!isMediaUri(uri)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bm = null;
        final ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                final int id = cursor.getInt(0);
                if (DEBUG)
                    Log.d(TAG, "Got thumb ID: " + id);

                if (mimeType.contains("video")) {
                    bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id,
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null);
                } else if (mimeType.contains(FileUtility.MIME_TYPE_IMAGE)) {
                    bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null);
                }
            }
        } catch (Exception e) {
            if (DEBUG)
                Log.e(TAG, "getThumbnail", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return bm;
    }

    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    public static Comparator<File> sComparator = (f1, f2) -> {
        // Sort alphabetically by lower case, which is much cleaner
        return f1.getName().toLowerCase().compareTo(
                f2.getName().toLowerCase());
    };

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sFileFilter = file -> {
        final String fileName = file.getName();
        // Return files only (not directories) and skip hidden files
        return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
    };

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sDirFilter = file -> {
        final String fileName = file.getName();
        // Return directories only and skip hidden directories
        return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
    };

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    public static Intent createGetContentIntent() {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    public static void deleteVideoCacheFile() {
        new Thread(() -> {
            File videoCacheFolder = new File(Constants.VIDEO_CACHE_FOLDER);
            if (!videoCacheFolder.exists()) {
                if (!videoCacheFolder.mkdirs()) {
                    Timber.e("can create folder at %s", Constants.VIDEO_CACHE_FOLDER);
                }
                return;
            }
            if (videoCacheFolder.listFiles() == null) {
                return;
            }
            int numberFiles = videoCacheFolder.listFiles().length;
            if (numberFiles > Constants.MAX_VIDEO_CACHE_NUMBER) {
                File[] files = videoCacheFolder.listFiles();
                Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

                for (int i = 0; i < files.length - Constants.MAX_VIDEO_CACHE_NUMBER; i++) {
                    deleteFile(files[i]);
                }
            }

        }).start();


    }

    public static void deleteFile(File file) {
        try {
            if (!file.delete()) {
                Timber.e("can delete file at %s", file.getPath());
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static boolean checkIfFolderExists(String fullFileName) {
        File f = new File(fullFileName);

        return f.exists() && f.isDirectory();
    }

    public static boolean createFolder(String folderPath) {
        File f = new File(folderPath);
        return f.mkdirs();
    }

    public static boolean saveBytes(byte[] data, String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(data);
            return true;
        } finally {
            fos.close();
        }
    }

    public static void writeFile(Context context, String fileName, String content, boolean isCopyToCachedFolder) throws IOException {
        FileOutputStream outputStream;
        outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();

        if (isCopyToCachedFolder) copyToCachedFolder(context, fileName);
    }

    private static void copyToCachedFolder(Context context, String fileName) throws IOException {
        try (FileInputStream inputStream = context.openFileInput(fileName); FileOutputStream cachedFile = new FileOutputStream(new File(Constants.FILE_CACHE_FOLDER, fileName))) {
            FileChannel inChannel = inputStream.getChannel();
            FileChannel outChannel = cachedFile.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (NullPointerException | FileNotFoundException e) {
            Timber.e(e);
        }
    }

    public static String readFilePrivate(Context context, String fileName) throws IOException {
        FileInputStream inputStream = context.openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public static String writeFileCached(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        File file = new File(Constants.FILE_CACHE_FOLDER, fileName);
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            Timber.e(e);
        }
        return sb.toString();
    }

    public static String readFileCached(String fileLink) throws IOException {
        StringBuilder sb = new StringBuilder();
        String fileName = fileLink.substring(fileLink.lastIndexOf('/'));
        File file = new File(Constants.FILE_CACHE_FOLDER, fileName);
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            Timber.e(e);
        }
        return sb.toString();
    }

    public static String loadJSONFromRaw(Context mContext, int RawID) {
        String json = null;
        try {
            InputStream is = mContext.getResources().openRawResource(RawID);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static File getOutputMediaFile(int type) {
        File mediaFile;
        // External sdcard location
        File mediaStorageDir;
        if (type == MEDIA_TYPE_IMAGE || type == MEDIA_TYPE_IMAGE_CROPPED) {
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMAGE_DIRECTORY_NAME);
        } else {
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    VIDEO_DIRECTORY_NAME);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                    + IMAGE_DIRECTORY_NAME + " directory");
            return null;
        }

        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "video" + ".mp4");
            if (mediaFile.exists()) {
                mediaFile.delete();
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "video" + ".mp4");
            }

        } else if (type == MEDIA_TYPE_IMAGE_CROPPED) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "image_cropped.jpg");
        } else {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "send.jpg");
        }
        return mediaFile;
    }
}