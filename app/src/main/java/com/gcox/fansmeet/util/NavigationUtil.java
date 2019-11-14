package com.gcox.fansmeet.util;

import com.gcox.fansmeet.core.activity.BaseActivity;


/**
 * Created by linh on 29/06/2017.
 */

public class NavigationUtil {
    public static void gotoProfileScreen(BaseActivity activity, String userName) {
        activity.startActivityProfile(0, userName);
    }
}
