package com.gcox.fansmeet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import timber.log.Timber;

/**
 * Created by User on 9/10/2015.
 */
public class DeviceInfo {

    @SuppressLint("HardwareIds")
    public static String getDeviceDetail(Context context) {
        String deviceId;
        try {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            deviceId = "";
            Timber.e(e);
        }

        return deviceId;
    }

}
