package com.gcox.fansmeet.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by sonnguyen on 9/14/15.
 */
public class Utils {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "m");
        suffixes.put(1_000_000_000L, "b");
        suffixes.put(1_000_000_000_000L, "t");
        suffixes.put(1_000_000_000_000_000L, "p");
        suffixes.put(1_000_000_000_000_000_000L, "e");
    }

    public static String formatThousand(long value) {
        return formatThousand(value, false);
    }

    public static String formatThousand(long value, boolean useIntValue) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) {
            return formatThousand(Long.MIN_VALUE + 1);
        }
        if (value < 0) {
            return "-" + formatThousand(-value);
        }
        if (value < 1000) {
            return Long.toString(value); //deal with easy case
        }

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        float truncated = (float) value / divideBy;
        truncated = ((float) (int) (truncated * 10)) / 10;
        if (truncated == (int) truncated) {
            return String.format(Locale.US, "%.0f%s", truncated, suffix);
        } else {
            return String.format(Locale.US, "%.1f%s", truncated, suffix);
        }
    }

    public static File getFileFromBitMap(Context mContext, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        File file = new File(mContext.getFilesDir(), "temp_bitmap.png");
        try {
            FileOutputStream os = new FileOutputStream(file);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            } finally {
                os.flush();
                os.close();
            }
        } catch (Exception e) {
        }
        return file;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (!activity.isFinishing()) {
                View v = activity.getWindow().getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Bitmap getBitmapFromURi(Context mContext, Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String shortenNumber(long value) {
        DecimalFormat format = new DecimalFormat("0.#");
        if (value > 1000000000) {//billion
            return format.format((double) value / 1000000000) + "b";
        } else if (value > 1000000) {//million
            return format.format((double) value / 1000000) + "m";
        } else if (value > 1000) {//thousand
            return format.format((double) value / 1000) + "k";
        } else {
            return format.format(value);
        }
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int dpToPx(float dp) {
        if (Resources.getSystem() == null) return 0;
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    public static boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static int getNavBarHeight(Resources resources) {
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        display.getRealSize(size);
        return size;
    }

    /**
     * Check if all of the requested permissions is granted
     *
     * @param context     the context
     * @param permissions an array of permission to check
     * @return true if all of the permissions is granted
     */
    public static boolean hasAllPermissionsGranted(@NonNull Context context, @NonNull String... permissions) {
        if (permissions.length < 1) {
            return false;
        }
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if there is at least 1 permission is not requestable because it has not been granted and user previously did select 'do not ask again'.
     *
     * @param activity    the activity
     * @param permissions the permission list
     * @return true if at least 1 permission among the requested permission is not requestable. False otherwise
     */
    public static boolean hasDoNotAskAgainPermissions(@NonNull Activity activity, boolean firstCheck, @NonNull String... permissions) {
        if (permissions.length < 1) {
            return false;
        }
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && !firstCheck) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter out all of the granted and not requestable permissions
     *
     * @param activity the context
     * @param src      the permission list
     * @return a list of requestable permission
     */
    @NonNull
    public static List<String> keepNotRequestedPermissions(@NonNull Activity activity, boolean firstCheck, @NonNull String[] src) {
        List<String> permissions = new ArrayList<>();
        if (src.length < 1) {
            return permissions;
        }
        for (String permission : src) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED &&
                    (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || firstCheck)) {
                permissions.add(permission);
            }
        }
        return permissions;
    }

    /**
     * Check permission from a permission source, then put the result into a map
     *
     * @param activity  the activity
     * @param src       the permissions to be checked
     * @param forceFill true to ignore the checking and use the fillValue as result
     * @param fillValue if forceFill is true, then it is the permission check result
     * @return the map contains all of the permission check
     */
    @NonNull
    public static Map<String, Boolean> fillPermissionResult(@NonNull Activity activity, @NonNull String[] src, boolean forceFill, boolean fillValue) {
        Map<String, Boolean> result = new HashMap<>();
        if (src.length < 1) {
            return result;
        }
        for (String permission : src) {
            if (forceFill) {
                result.put(permission, fillValue);
            } else {
                result.put(permission, ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return result;
    }

    /**
     * Merge a permission array to an source array
     *
     * @param src  the permission source
     * @param args the new permissions to add
     * @return a new array which contains the merge result
     */
    public static String[] appendPermissions(@NonNull String[] src, String... args) {
        List<String> result = new ArrayList<>(Arrays.asList(src));
        if (args != null) {
            result.addAll(Arrays.asList(args));
        }
        return result.toArray(new String[0]);
    }
}
