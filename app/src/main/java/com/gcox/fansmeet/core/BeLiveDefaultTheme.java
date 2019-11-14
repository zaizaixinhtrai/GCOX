package com.gcox.fansmeet.core;

import com.gcox.fansmeet.R;

/**
 * Created by thanhbc on 11/27/17.
 */

public class BeLiveDefaultTheme implements BeLiveThemeHelper {
    @Override
    public int getAppMenuIcon() {
        return 0;
    }

    @Override
    public int getAppEditProfileIcon() {
        return R.drawable.ic_edit_profile;
    }

    @Override
    public int getAppOptionMenuIcon() {
        return 0;
    }

    @Override
    public int getAppNotificationBellIconLight() {
        return 0;
    }

    @Override
    public int getAppMenuIconLight() {
        return 0;
    }

    @Override
    public int getAppNotificationBellIcon() {
        return 0;
    }

    @Override
    public int getLiveTagIcon() {
        return 0;
    }

    @Override
    public int getRecordedTagIcon() {
        return 0;
    }

    @Override
    public int getNavHomeIcon() {
        return R.drawable.nav_home_selector;
    }

    @Override
    public int getNavNewFeedsIcon() {
        return 0;
    }

    @Override
    public int getNavNotificationIcon() {
        return R.drawable.nav_notification_selector;
    }

    @Override
    public int getNavProfileIcon() {
        return R.drawable.nav_profile_selector;
    }

    @Override
    public int getToolbarBackIcon() {
        return R.drawable.icon_back_btn_white;
    }

    @Override
    public int getNavLiveIcon() {
        return 0;
    }

    @Override
    public int getToolbarTopPadding(){
        return 0;
    }

    @Override
    public boolean isTransparentStatusBarRequired() {
        return false;
    }

    @Override
    public int getNavChallengesIcon() {
        return R.drawable.nav_challenges_selector;
    }
}
