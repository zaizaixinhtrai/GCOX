package com.gcox.fansmeet.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.BuildConfig;
import com.gcox.fansmeet.R;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by USER on 10/12/2015.
 */
public class AppsterUtility {

    public static String convertTime(int time) {
        String timeConvert = "";
        try {
            Date tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(String.valueOf(time));
            timeConvert = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(tradeDate);
        } catch (ParseException error) {
            error.printStackTrace();
        }
        return timeConvert;
    }
//    Following: 1
//            - Comment: 2
//            - Like: 3
//            - Receive gift: 4
//            - Receive commission (referral): 5
//            - Invite friend : 6

    public static String convertDateTimeString(Date date, String timeFormat) {
        if (date == null) return "";
        // convert day 'th' format
        if (timeFormat.contains("'th'")) {
            String thString = null;
            SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.getDefault());
            sdf.setTimeZone(Calendar.getInstance().getTimeZone());
            String dateString = sdf.format(date);
            if (dateString.endsWith("1") && !dateString.endsWith("11")) {
                thString = "st";
            } else if (dateString.endsWith("2") && !dateString.endsWith("12")) {
                thString = "nd";
            } else if (dateString.endsWith("3") && !dateString.endsWith("13")) {
                thString = "rd";
            }
            if (thString != null) {
                timeFormat = timeFormat.replace("'th'", String.format("'%s'", thString));
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        return sdf.format(date);
    }

    public static String convertRelativeTimeSpanStringDay(Context context, Date dateToConvert) {
        long days = (new Date().getTime() - dateToConvert.getTime()) / DateUtils.DAY_IN_MILLIS;
        if (days == 0) return context.getString(R.string.chattime_today);
        else if (days == 1) return context.getString(R.string.chattime_yesterday);
        else
            return convertDateTimeString(dateToConvert.getTime(), context.getString(R.string.formatter_chat_date));

//                    String.valueOf(DateUtils.getRelativeTimeSpanString(dateToConvert.getTime(), System.currentTimeMillis(),DateUtils.DAY_IN_MILLIS));
    }

    /**
     * convert a dd/mm/yyyy string format to dd MMM yyy string format
     * example input 22/09/1990
     * example output 22 Sep 1990
     */
    public static String convertDateTime(String time) {
        SimpleDateFormat oldSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat newSdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String result = "";
        try {
            Date date = oldSdf.parse(time);
            result = newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String convertDateTimeString(long milliseconds, String timeFormat) {
        if (milliseconds == 0) return "";
        return convertDateTimeString(new Date(milliseconds), timeFormat);
    }

    public static boolean sameDate(Date date1, Date date2) {
//        if (date1 == null || date2 == null) {
//            return false;
//        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }


    static String getRealPathFromURI(Uri contentURI, Activity activity) {
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

    public static String parseTimeToHHMM(long seconds) {
        long hr = seconds / 3600;
        long rem = seconds % 3600;
        long mn = rem / 60;
        long sec = rem % 60;
        String hrStr = (hr < 10 ? "0" : "") + hr;
        String mnStr = (mn < 10 ? "0" : "") + mn;
        String secStr = (sec < 10 ? "0" : "") + sec;
        return hrStr + ":" + mnStr + ":" + secStr;
    }

    public static String parseStreamingTimeToHHMM(long elapsedTime) {

        long hours = elapsedTime / 3600L,
                minutes = (elapsedTime / 60L) % 60L,
                seconds = elapsedTime % 60L;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static final long CLICK_THROTTLE_DURATION = 1000L;

    public static Observable<Object> clicks(View view) {
        Observable<Object> observable = RxView.clicks(view);
//        if (!BuildConfig.DEBUG) {
        observable = observable.throttleFirst(CLICK_THROTTLE_DURATION, TimeUnit.MILLISECONDS);
//        }
        return observable.observeOn(AndroidSchedulers.mainThread());
    }

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * temporary lock a view in 1000 milliseconds
     * to prevent multi click in a short period of time
     *
     * @param view to be locked
     */
    public static void temporaryLockView(View view) {
        view.setEnabled(false);
        view.postDelayed(() -> view.setEnabled(true), CLICK_THROTTLE_DURATION);
    }

    //region sharepref utils
    private static void storePrefList(Context context, String pref_name, String key, List results) {
        if (context == null) {
            return;
        }
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(results);
        editor.putString(key, jsonFavorites);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static ArrayList<String> loadPrefList(Context context, String pref_name, String key) {

        SharedPreferences settings;
        List<String> favorites;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        if (settings.contains(key)) {
            String jsonFavorites = settings.getString(key, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return new ArrayList<>();
        return (ArrayList<String>) favorites;
    }

    public static void addPrefListItem(Context context, String pref_name, String key, String content) {
        List<String> favorites = loadPrefList(context, pref_name, key);
        if (favorites == null)
            favorites = new ArrayList<>();

        if (favorites.contains(content)) {

            favorites.remove(content);

        }
        favorites.add(content);

        storePrefList(context, pref_name, key, favorites);

    }

    public static void removePrefListItem(Context context, String pref_name, String key, String content) {
        List<String> favorites = loadPrefList(context, pref_name, key);
        if (favorites == null)
            favorites = new ArrayList<>();

        if (favorites.contains(content)) {

            favorites.remove(content);

        } else {
            return;
        }

        storePrefList(context, pref_name, key, favorites);

    }

    public static void resetPrefListByKey(Context context, String pref_name, String key) {
        storePrefList(context, pref_name, key, new ArrayList<>());
    }


    public static <T> List<T> loadPrefListObject(Context context, String pref_name, String key, Class<T[]> clazz) {

        SharedPreferences settings;
        T[] chatMessages;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        if (settings.contains(key)) {
            String jsonFavorites = settings.getString(key, null);
            Gson gson = new Gson();
            chatMessages = gson.fromJson(jsonFavorites, clazz);
        } else
            return new ArrayList<>();
        return Arrays.asList(chatMessages);
    }

    public static void storePrefListObject(Context context, String pref_name, String key, List results) {
        if (context == null) {
            return;
        }
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(results);
        editor.putString(key, jsonFavorites);
        editor.apply();
    }

    public static void deletePrefListObjectByKey(Context context, String pref_name, String key) {
        if (context == null) {
            return;
        }
        SharedPreferences settings;
        settings = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        if (settings.contains(key)) {
            settings.edit().remove(key).apply();
            Timber.e("remove listMessage of slug %s", key);
        }
    }


    public static void deletePrefList(Context context, String pref_name) {

        SharedPreferences myPrefs = context.getSharedPreferences(pref_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.apply();
    }

    //endregion
    public static File persistImage(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("AppstersUtility", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }


    public static void goToPlayStore(Activity activity, int requestCode) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeCustomAnimation(activity, R.anim.push_in_to_right, R.anim.push_in_to_left);
        final String appPackageName = BuildConfig.APPLICATION_ID
                ;//getPackageName(); // getPackageName() from Context or Activity object
        try {
            ActivityCompat.startActivityForResult(activity, new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)), requestCode, options.toBundle());
        } catch (android.content.ActivityNotFoundException anfe) {
            Timber.d(anfe);
            ActivityCompat.startActivityForResult(activity, new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)), requestCode, options.toBundle());
        }
    }

    public static String getAuth(){
        return "Bearer " + AppsterApplication.mAppPreferences.getUserToken();
    }
}
