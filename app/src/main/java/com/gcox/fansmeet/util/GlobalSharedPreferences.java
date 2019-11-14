package com.gcox.fansmeet.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by linh on 07/09/2017.
 */

public class GlobalSharedPreferences {
    private static final String APP_GLOBAL_SHARED_PREFERENCES = "APP_GLOBAL_SHARED_PREFERENCES";
    private static final String TUTORIAL_HOST_STREAM_SHOWN = "TUTORIAL_HOST_STREAM_SHOWN";
    private static final String TUTORIAL_VIEWER_STREAM_SHOWN = "TUTORIAL_VIEWER_STREAM_SHOWN";

    public static boolean isTutorialHostStreamShown(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(TUTORIAL_HOST_STREAM_SHOWN, false);
    }

    public static void setTutorialHostStreamShown(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TUTORIAL_HOST_STREAM_SHOWN, true);
        editor.apply();
    }

    public static boolean isTutorialViewerStreamShown(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(TUTORIAL_VIEWER_STREAM_SHOWN, false);
    }

    public static void setTutorialViewerStreamShown(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_GLOBAL_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TUTORIAL_VIEWER_STREAM_SHOWN, true);
        editor.apply();
    }
}
