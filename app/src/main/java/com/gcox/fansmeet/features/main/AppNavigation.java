package com.gcox.fansmeet.features.main;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.gcox.fansmeet.features.main.AppNavigation.*;


/**
 * Created by thanhbc on 6/3/17.
 */

@IntDef({HOME, CHALLENGES, SEARCH, ACTIONS, PROFILE})
@Retention(RetentionPolicy.SOURCE)
public @interface AppNavigation {
    int HOME = 0;
    int SEARCH = 2;
    int CHALLENGES = 1;
    int ACTIONS = 4;
    int PROFILE = 3;
}
