package com.gcox.fansmeet.features.main;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.gcox.fansmeet.features.main.UserType.*;


/**
 * Created by thanhbc on 6/3/17.
 */

@IntDef({NORMAL_USER, CELEBRITY_USER, MERCHANT_USER, INFLUENCER_USER,EVENT_USER})
@Retention(RetentionPolicy.SOURCE)
public @interface UserType {
    int NORMAL_USER = 0;
    int CELEBRITY_USER = 1;
    int MERCHANT_USER = 2;
    int INFLUENCER_USER = 3;
    int EVENT_USER = 4;
}
