package com.gcox.fansmeet.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    boolean haveConnectedActive = false;
    ConnectivityManager connMgr;
    NetworkInfo wifi;
    NetworkInfo mobile;

    Context mContext;

    public CheckNetwork(Context mContext) {
        this.mContext = mContext;
    }

    /*
     * Checking the network is available or not
     */
    public boolean checkNetwork() {

        connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            haveConnectedActive = true;
        }

        if (wifi != null && wifi.isAvailable()) {
            haveConnectedWifi = true;
        }

        if (mobile != null && mobile.isAvailable()) {
            haveConnectedMobile = true;
        }

        // if network is connected and either wifi or mobile available means
        // return true , else false
        return (haveConnectedWifi || haveConnectedMobile)
                && haveConnectedActive;
    }


    public static boolean isNetworkAvailable(Context context) {

        if (context == null) return false;

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}