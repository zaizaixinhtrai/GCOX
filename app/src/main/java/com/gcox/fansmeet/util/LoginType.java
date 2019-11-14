package com.gcox.fansmeet.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by linh on 20/09/2017.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({LoginType.LOGIN_FROM_FACEBOOK, LoginType.LOGIN_FROM_PLAY_TOKEN, LoginType.LOGIN_FROM_INSTAGRAM, LoginType.LOGIN_FROM_GOOGLE, LoginType.LOGIN_FROM_TWITTER, LoginType.LOGIN_FROM_EMAIL, LoginType.LOGIN_FROM_PHONE})
public @interface LoginType {
    int LOGIN_FROM_FACEBOOK = 0;
    int LOGIN_FROM_PLAY_TOKEN = 1;
    int LOGIN_FROM_INSTAGRAM = 2;
    int LOGIN_FROM_GOOGLE = 3;
    int LOGIN_FROM_TWITTER = 4;
    int LOGIN_FROM_EMAIL = 5;
    int LOGIN_FROM_PHONE = 6;
}
